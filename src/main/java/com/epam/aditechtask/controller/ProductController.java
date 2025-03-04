package com.epam.aditechtask.controller;

import com.epam.aditechtask.dto.ProductRequest;
import com.epam.aditechtask.dto.ProductResponse;
import com.epam.aditechtask.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product management APIs")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get all products", description = "Retrieve a list of all products")
	public List<ProductResponse> getAllProducts(@RequestParam(defaultValue = "EUR") String currency) {
		return productService.getAllProducts(currency);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get a product by ID", description = "Retrieve details of a specific product by its ID")
	public ProductResponse getProductById(@PathVariable Long id, @RequestParam(defaultValue = "EUR") String currency) {
		return productService.getProductById(id, currency);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create a new product", description = "Add a new product to the database")
	public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest dto) {
		ProductResponse createdProduct = productService.createProduct(dto);
		URI location = URI.create("/products/" + createdProduct.id());
		return ResponseEntity.created(location).body(createdProduct);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Update product", description = "Updates product data")
	public ProductResponse updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest dto) {
		return productService.updateProduct(id, dto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete a product", description = "Remove a product by its ID")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}