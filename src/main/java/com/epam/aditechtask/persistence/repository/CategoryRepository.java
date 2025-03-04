package com.epam.aditechtask.persistence.repository;

import com.epam.aditechtask.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	boolean existsByParentId(Long parentId);
}
