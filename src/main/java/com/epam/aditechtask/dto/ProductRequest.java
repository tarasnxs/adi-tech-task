package com.epam.aditechtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

public record ProductRequest(
	@NotBlank
	@Schema(description = "Product name", example = "IPhone 13 Pro", requiredMode = RequiredMode.REQUIRED)
	String name,
	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	@Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
	@Schema(description = "Product price", example = "250.15", requiredMode = RequiredMode.REQUIRED)
	BigDecimal price,
	@NotNull
	@Schema(description = "Product category", example = "1", requiredMode = RequiredMode.REQUIRED)
	Long categoryId,
	@Schema(description = "Price currency (optional) EUR by default", example = "USD", requiredMode = RequiredMode.NOT_REQUIRED)
	Optional<String> currency
) {}
