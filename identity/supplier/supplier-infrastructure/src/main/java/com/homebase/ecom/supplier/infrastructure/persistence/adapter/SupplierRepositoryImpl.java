package com.homebase.ecom.supplier.infrastructure.persistence.adapter;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierRepository;
import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;
import com.homebase.ecom.supplier.infrastructure.persistence.mapper.SupplierMapper;

import java.util.Optional;

/**
 * Implementation of domain SupplierRepository port.
 * Delegates to Spring Data SupplierJpaRepository and maps between domain/JPA.
 * No Spring annotations here — wired in SupplierConfiguration.
 */
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierJpaRepository jpaRepository;
    private final SupplierMapper mapper;

    public SupplierRepositoryImpl(SupplierJpaRepository jpaRepository, SupplierMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Supplier> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Supplier supplier) {
        SupplierEntity entity = mapper.toEntity(supplier);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
