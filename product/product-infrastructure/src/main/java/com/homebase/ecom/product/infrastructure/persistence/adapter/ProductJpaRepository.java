package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    List<ProductEntity> findBySupplierIdAndStateId(String supplierId, String stateId);
    List<ProductEntity> findBySupplierId(String supplierId);
}
