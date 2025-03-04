package com.epam.aditechtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

public record CategoryRequest(
	@NotBlank
	@Schema(description = "Category name", example = "Electronics", requiredMode = RequiredMode.REQUIRED)
	String name,
	@Schema(description = "Id of parent category", example = "1", requiredMode = RequiredMode.NOT_REQUIRED)
	Optional<Long> parentId
) {}
