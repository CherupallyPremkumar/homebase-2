package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
}
