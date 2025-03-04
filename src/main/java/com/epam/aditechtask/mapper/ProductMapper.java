package com.epam.aditechtask.mapper;

import com.epam.aditechtask.dto.ProductRequest;
import com.epam.aditechtask.dto.ProductResponse;
import com.epam.aditechtask.persistence.entity.CategoryEntity;
import com.epam.aditechtask.persistence.entity.ProductEntity;
import com.epam.aditechtask.persistence.repository.CategoryRepository;
import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = CategoryRepository.class)
public interface ProductMapper {

	@Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
	@Mapping(target = "priceEur", expression = "java(request.price().divide(rate, 2, BigDecimal.ROUND_HALF_UP))")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	ProductEntity toEntity(ProductRequest request, @Context BigDecimal rate);

	@Mapping(source = "category.id", target = "categoryId")
	@Mapping(target = "price", expression = "java(product.getPriceEur().multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP))")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	ProductResponse toResponse(ProductEntity product, @Context String currency, @Context BigDecimal rate);

	@Named("mapCategory")
	default CategoryEntity mapCategory(Long categoryId) {
		CategoryEntity category = new CategoryEntity();
		category.setId(categoryId);
		return category;
	}

	@AfterMapping
	default void setCurrency(@MappingTarget ProductResponse.ProductResponseBuilder response, @Context String currency) {
		response.currency(currency);
	}
}