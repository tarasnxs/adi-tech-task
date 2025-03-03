package com.epam.aditechtask.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "category")
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private CategoryEntity parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<CategoryEntity> subcategories;
}
