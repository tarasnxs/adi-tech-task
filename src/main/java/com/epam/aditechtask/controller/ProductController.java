package com.epam.aditechtask.controller;

import com.epam.aditechtask.dto.ProductDTO;
import com.epam.aditechtask.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get all products", description = "Retrieve a list of all products")
	public List<ProductDTO> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get a product by ID", description = "Retrieve details of a specific product by its ID")
	public ProductDTO getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create a new product", description = "Add a new product to the database")
	public ProductDTO createProduct(@RequestBody ProductDTO dto) {
		return productService.createProduct(dto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete a product", description = "Remove a product by its ID")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}