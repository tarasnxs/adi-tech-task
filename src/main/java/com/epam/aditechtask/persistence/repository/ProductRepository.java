package com.epam.aditechtask.persistence.repository;

import com.epam.aditechtask.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
