package com.epam.aditechtask.controller;

import com.epam.aditechtask.dto.CategoryDTO;
import com.epam.aditechtask.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
	public List<CategoryDTO> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Operation(summary = "Get a category by ID", description = "Retrieve details of a specific category by its ID")
	public CategoryDTO getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create a new category", description = "Add a new category to the database")
	public CategoryDTO createCategory(@RequestBody CategoryDTO dto) {
		return categoryService.createCategory(dto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete a category", description = "Remove a category by its ID")
	public void deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
}