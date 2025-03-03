package com.epam.aditechtask.service;

import com.epam.aditechtask.dto.CategoryDTO;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import com.epam.aditechtask.persistence.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll()
			.stream()
			.map(category -> new CategoryDTO(category.getId(), category.getName(),
				category.getParent() != null ? category.getParent().getId() : null))
			.toList();
	}

	public CategoryDTO getCategoryById(Long id) {
		return categoryRepository.findById(id)
			.map(category -> new CategoryDTO(category.getId(), category.getName(),
				category.getParent() != null ? category.getParent().getId() : null))
			.orElseThrow(() -> new RuntimeException("Category not found"));
	}

	public CategoryDTO createCategory(CategoryDTO dto) {
		CategoryEntity parent = dto.parentId() != null ? categoryRepository.findById(dto.parentId())
			.orElseThrow(() -> new RuntimeException("Parent category not found")) : null;
		CategoryEntity category = new CategoryEntity();
		category.setName(dto.name());
		category.setParent(parent);
		category = categoryRepository.save(category);
		return new CategoryDTO(category.getId(), category.getName(), dto.parentId());
	}

	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}
}
