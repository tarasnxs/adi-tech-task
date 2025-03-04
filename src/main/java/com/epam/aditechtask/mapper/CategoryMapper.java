package com.epam.aditechtask.mapper;

import com.epam.aditechtask.dto.CategoryRequest;
import com.epam.aditechtask.dto.CategoryResponse;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import java.util.Optional;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	@Mapping(source = "parent.id", target = "parentId")
	CategoryResponse toDto(CategoryEntity category);

	@Mapping(source = "parentId", target = "parent", qualifiedByName = "mapParent")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	CategoryEntity toEntity(CategoryRequest dto);

	@Named("mapParent")
	default CategoryEntity mapParent(Optional<Long> parentId) {
		if (parentId.isEmpty()) return null;
		CategoryEntity parent = new CategoryEntity();
		parent.setId(parentId.get());
		return parent;
	}
}