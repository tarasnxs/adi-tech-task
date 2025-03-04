package com.epam.aditechtask.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductResponse(Long id, String name, BigDecimal price, String currency, Long categoryId) {

}
