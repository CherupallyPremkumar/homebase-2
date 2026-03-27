package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    @Query(value = "SELECT * FROM products p WHERE p.supplier_id = :supplierId AND p.state_id = :stateId", nativeQuery = true)
    List<ProductEntity> findBySupplierIdAndStateId(@Param("supplierId") String supplierId, @Param("stateId") String stateId);

    List<ProductEntity> findBySupplierId(String supplierId);
}
