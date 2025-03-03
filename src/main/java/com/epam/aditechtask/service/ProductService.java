package com.epam.aditechtask.service;

import com.epam.aditechtask.dto.ProductDTO;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import com.epam.aditechtask.persistence.entity.ProductEntity;
import com.epam.aditechtask.persistence.repository.CategoryRepository;
import com.epam.aditechtask.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll()
			.stream()
			.map(product -> new ProductDTO(product.getId(), product.getName(), product.getPriceEur(), product.getCategory().getId()))
			.toList();
	}

	public ProductDTO getProductById(Long id) {
		return productRepository.findById(id)
			.map(product -> new ProductDTO(product.getId(), product.getName(), product.getPriceEur(), product.getCategory().getId()))
			.orElseThrow(() -> new RuntimeException("Product not found"));
	}

	public ProductDTO createProduct(ProductDTO dto) {
		CategoryEntity category = categoryRepository.findById(dto.categoryId())
			.orElseThrow(() -> new RuntimeException("Category not found"));

		ProductEntity product = new ProductEntity();
		product.setName(dto.name());
		product.setPriceEur(dto.price());
		product.setCategory(category);
		product = productRepository.save(product);

		return new ProductDTO(product.getId(), product.getName(), product.getPriceEur(), product.getCategory().getId());
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
