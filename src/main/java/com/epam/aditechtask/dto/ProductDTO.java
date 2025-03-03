package com.epam.aditechtask.dto;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, BigDecimal price, Long categoryId) { }
