package com.epam.aditechtask.service;

import com.epam.aditechtask.dto.CategoryRequest;
import com.epam.aditechtask.dto.CategoryResponse;
import com.epam.aditechtask.exception.ResourceNotFoundException;
import com.epam.aditechtask.mapper.CategoryMapper;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import com.epam.aditechtask.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private CategoryMapper categoryMapper;

	@InjectMocks
	private CategoryService categoryService;

	private CategoryEntity category;
	private CategoryResponse categoryResponse;
	private CategoryRequest categoryRequest;

	@BeforeEach
	void setUp() {
		category = new CategoryEntity();
		category.setId(1L);
		category.setName("Electronics");

		categoryResponse = new CategoryResponse(1L, "Electronics", null);
		categoryRequest = new CategoryRequest("Electronics", Optional.empty());
	}

	@Test
	void getAllCategories_ShouldReturnList() {
		when(categoryRepository.findAll()).thenReturn(List.of(category));
		when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

		List<CategoryResponse> result = categoryService.getAllCategories();

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().name()).isEqualTo("Electronics");

		verify(categoryRepository, times(1)).findAll();
		verify(categoryMapper, times(1)).toDto(category);
	}

	@Test
	void getCategoryById_WhenExists_ShouldReturnCategory() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

		CategoryResponse result = categoryService.getCategoryById(1L);

		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("Electronics");

		verify(categoryRepository, times(1)).findById(1L);
		verify(categoryMapper, times(1)).toDto(category);
	}

	@Test
	void getCategoryById_WhenNotExists_ShouldThrowException() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.getCategoryById(1L))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Category not found");

		verify(categoryRepository, times(1)).findById(1L);
		verifyNoInteractions(categoryMapper);
	}

	@Test
	void createCategory_WhenParentExists_ShouldCreateCategory() {
		when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);
		when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

		CategoryResponse result = categoryService.createCategory(categoryRequest);

		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("Electronics");

		verify(categoryRepository, times(1)).save(category);
		verify(categoryMapper, times(1)).toDto(category);
	}

	@Test
	void createCategory_WhenParentDoesNotExist_ShouldThrowException() {
		CategoryRequest requestWithParent = new CategoryRequest("Phones", Optional.of(2L));
		when(categoryRepository.existsById(2L)).thenReturn(false);

		assertThatThrownBy(() -> categoryService.createCategory(requestWithParent))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Parent category not found");

		verify(categoryRepository, times(1)).existsById(2L);
		verifyNoMoreInteractions(categoryRepository, categoryMapper);
	}

	@Test
	void updateCategory_WhenCategoryExists_ShouldUpdateAndReturn() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(categoryRepository.save(category)).thenReturn(category);
		when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

		CategoryResponse result = categoryService.updateCategory(1L, categoryRequest);

		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("Electronics");

		verify(categoryRepository, times(1)).findById(1L);
		verify(categoryRepository, times(1)).save(category);
		verify(categoryMapper, times(1)).toDto(category);
	}

	@Test
	void updateCategory_WhenCategoryNotFound_ShouldThrowException() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.updateCategory(1L, categoryRequest))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Category not found");

		verify(categoryRepository, times(1)).findById(1L);
		verifyNoInteractions(categoryMapper);
	}

	@Test
	void updateCategory_WhenParentIsSelf_ShouldThrowException() {
		CategoryRequest requestWithSelfParent = new CategoryRequest("Phones", Optional.of(1L));

		assertThatThrownBy(() -> categoryService.updateCategory(1L, requestWithSelfParent))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("A category cannot be its own parent");

		verifyNoInteractions(categoryRepository, categoryMapper);
	}

	@Test
	void deleteCategory_WhenCategoryExists_ShouldDelete() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		when(categoryRepository.existsByParentId(1L)).thenReturn(false);

		categoryService.deleteCategory(1L);

		verify(categoryRepository, times(1)).existsById(1L);
		verify(categoryRepository, times(1)).existsByParentId(1L);
		verify(categoryRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteCategory_WhenCategoryHasChildren_ShouldThrowException() {
		when(categoryRepository.existsById(1L)).thenReturn(true);
		when(categoryRepository.existsByParentId(1L)).thenReturn(true);

		assertThatThrownBy(() -> categoryService.deleteCategory(1L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("Cannot delete category with subcategories.");

		verify(categoryRepository, times(1)).existsById(1L);
		verify(categoryRepository, times(1)).existsByParentId(1L);
		verify(categoryRepository, never()).deleteById(1L);
	}

	@Test
	void deleteCategory_WhenCategoryNotFound_ShouldThrowException() {
		when(categoryRepository.existsById(1L)).thenReturn(false);

		assertThatThrownBy(() -> categoryService.deleteCategory(1L))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Category not found");

		verify(categoryRepository, times(1)).existsById(1L);
		verifyNoMoreInteractions(categoryRepository);
	}
}