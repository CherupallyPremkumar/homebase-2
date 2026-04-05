package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantJpaRepository extends JpaRepository<VariantEntity, String> {
}
