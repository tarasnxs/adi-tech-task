package com.epam.aditechtask.service;

import com.epam.aditechtask.dto.CategoryRequest;
import com.epam.aditechtask.dto.CategoryResponse;
import com.epam.aditechtask.exception.ResourceNotFoundException;
import com.epam.aditechtask.mapper.CategoryMapper;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import com.epam.aditechtask.persistence.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll().stream()
			.map(categoryMapper::toDto)
			.toList();
	}

	public CategoryResponse getCategoryById(Long id) {
		return categoryRepository.findById(id)
			.map(categoryMapper::toDto)
			.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
	}

	public CategoryResponse createCategory(CategoryRequest dto) {
		if (dto.parentId().isPresent() && !categoryRepository.existsById(dto.parentId().get())) {
			throw new ResourceNotFoundException("Parent category not found");
		}
		return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(dto)));
	}

	@Transactional
	public CategoryResponse updateCategory(Long id, CategoryRequest dto) {
		if (dto.parentId().isPresent() && dto.parentId().get().equals(id)) {
			throw new IllegalStateException("A category cannot be its own parent");
		}
		CategoryEntity category = categoryRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		dto.parentId().map(categoryRepository::findById)
			.ifPresentOrElse(parent -> category.setParent(
				parent.orElseThrow(() -> new ResourceNotFoundException("Parent not found"))),
				() -> category.setParent(null));
		category.setName(dto.name());
		return categoryMapper.toDto(categoryRepository.save(category));
	}

	public void deleteCategory(Long id) {
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Category not found");
		}
		if (categoryRepository.existsByParentId(id)) {
			throw new IllegalStateException("Cannot delete category with subcategories.");
		}
		categoryRepository.deleteById(id);
	}
}
