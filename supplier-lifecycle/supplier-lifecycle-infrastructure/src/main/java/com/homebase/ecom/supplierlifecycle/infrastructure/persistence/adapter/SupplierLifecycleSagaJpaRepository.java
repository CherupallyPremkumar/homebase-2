package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.adapter;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaEntity;

public interface SupplierLifecycleSagaJpaRepository extends JpaRepository<SupplierLifecycleSagaEntity, String> {
    List<SupplierLifecycleSagaEntity> findBySupplierId(String supplierId);
}
