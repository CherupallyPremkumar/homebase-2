package com.homebase.ecom.supplier.infrastructure.persistence;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.infrastructure.persistence.adapter.SupplierJpaRepository;
import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;
import com.homebase.ecom.supplier.infrastructure.persistence.mapper.SupplierMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for Supplier.
 * Uses ChenileJpaEntityStore which properly handles:
 * - Optimistic locking via @Version (loads existing entity before update)
 * - Merge function to copy fields from updated entity to managed entity
 * - ID propagation back to domain model after persist
 */
public class ChenileSupplierEntityStore extends ChenileJpaEntityStore<Supplier, SupplierEntity> {

    public ChenileSupplierEntityStore(SupplierJpaRepository repository, SupplierMapper mapper) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
