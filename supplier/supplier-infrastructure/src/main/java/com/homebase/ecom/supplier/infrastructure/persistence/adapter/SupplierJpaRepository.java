package com.homebase.ecom.supplier.infrastructure.persistence.adapter;

import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for SupplierEntity.
 */
@Repository
public interface SupplierJpaRepository extends JpaRepository<SupplierEntity, String> {
}
