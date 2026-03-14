package com.homebase.ecom.supplier.infrastructure.persistence;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.infrastructure.persistence.adapter.SupplierJpaRepository;
import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;
import com.homebase.ecom.supplier.infrastructure.persistence.mapper.SupplierMapper;
import org.chenile.base.exception.NotFoundException;
import org.chenile.utils.entity.service.EntityStore;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence.
 * Uses SupplierJpaRepository directly and maps via SupplierMapper.
 */
public class ChenileSupplierEntityStore implements EntityStore<Supplier> {

    private final SupplierJpaRepository jpaRepository;
    private final SupplierMapper mapper;

    public ChenileSupplierEntityStore(SupplierJpaRepository jpaRepository, SupplierMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void store(Supplier supplier) {
        SupplierEntity entity = mapper.toEntity(supplier);
        jpaRepository.save(entity);
    }

    @Override
    public Supplier retrieve(String id) {
        SupplierEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(1500, "Supplier not found: " + id));
        return mapper.toModel(entity);
    }
}
