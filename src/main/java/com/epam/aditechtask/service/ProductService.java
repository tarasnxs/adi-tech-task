package com.epam.aditechtask.service;

import com.epam.aditechtask.dto.ProductRequest;
import com.epam.aditechtask.dto.ProductResponse;
import com.epam.aditechtask.exception.ResourceNotFoundException;
import com.epam.aditechtask.mapper.ProductMapper;
import com.epam.aditechtask.persistence.entity.ProductEntity;
import com.epam.aditechtask.persistence.repository.ProductRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private static final String DEFAULT_CURRENCY_CODE = "EUR";

	private final ProductRepository productRepository;
	private final ExchangeRateService exchangeRateService;
	private final ProductMapper productMapper;

	public List<ProductResponse> getAllProducts(String currency) {
		return productRepository.findAll().stream()
			.map(product -> productMapper.toResponse(product, currency, getRateForCurrency(currency)))
			.toList();
	}

	public ProductResponse getProductById(Long id, String currency) {
		ProductEntity product = productRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		return productMapper.toResponse(product, currency, getRateForCurrency(currency));
	}

	public ProductResponse createProduct(ProductRequest request) {
		BigDecimal rate = getRateForRequest(request);
		ProductEntity product = productMapper.toEntity(request, rate);
		return productMapper.toResponse(productRepository.save(product), request.currency().orElse(DEFAULT_CURRENCY_CODE), rate);
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		ProductEntity product = productRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		BigDecimal rate = getRateForRequest(request);
		product.setPriceEur(request.price().divide(rate, 2, RoundingMode.HALF_UP));


		return productMapper.toResponse(productRepository.save(product), request.currency().orElse(DEFAULT_CURRENCY_CODE), rate);
	}

	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found");
		}
		productRepository.deleteById(id);
	}

	private BigDecimal getRateForRequest(ProductRequest request) {
		if (request.currency().isPresent() && !request.currency().get().equals(DEFAULT_CURRENCY_CODE)) {
			return exchangeRateService.getExchangeRate(request.currency().get());
		} else {
			return BigDecimal.ONE;
		}
	}

	private BigDecimal getRateForCurrency(String currency) {
		if (currency.equals(DEFAULT_CURRENCY_CODE)) {
			return BigDecimal.ONE;
		} else {
			return exchangeRateService.getExchangeRate(currency);
		}
	}
}
