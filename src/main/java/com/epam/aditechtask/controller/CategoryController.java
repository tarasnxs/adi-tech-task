package com.epam.aditechtask.controller;

import com.epam.aditechtask.dto.CategoryRequest;
import com.epam.aditechtask.dto.CategoryResponse;
import com.epam.aditechtask.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management APIs")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
	public List<CategoryResponse> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get a category by ID", description = "Retrieve details of a specific category by its ID")
	public CategoryResponse getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create a new category", description = "Add a new category to the database")
	public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest dto) {
		CategoryResponse createdCategory = categoryService.createCategory(dto);
		URI location = URI.create("/categories/" + createdCategory.id());
		return ResponseEntity.created(location).body(createdCategory);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Updates category", description = "Updates category data")
	public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest dto) {
		return categoryService.updateCategory(id, dto);
	}


	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete a category", description = "Remove a category by its ID")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
}