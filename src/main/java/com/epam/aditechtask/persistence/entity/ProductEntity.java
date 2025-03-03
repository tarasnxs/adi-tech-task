package com.epam.aditechtask.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "product")
@Data
@EqualsAndHashCode(callSuper=true)
public class ProductEntity extends BaseEntity {

	@Column(name = "price_eur", precision = 10, scale = 2, nullable = false)
	private BigDecimal priceEur;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryEntity category;
}