package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import com.homebase.ecom.supplierlifecycle.domain.port.SupplierLifecycleSagaRepository;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaEntity;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.mapper.SupplierLifecycleSagaMapper;

public class SupplierLifecycleSagaRepositoryImpl implements SupplierLifecycleSagaRepository {

    private final SupplierLifecycleSagaJpaRepository jpaRepository;
    private final SupplierLifecycleSagaMapper mapper;

    public SupplierLifecycleSagaRepositoryImpl(SupplierLifecycleSagaJpaRepository jpaRepository,
                                                SupplierLifecycleSagaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SupplierLifecycleSaga> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<SupplierLifecycleSaga> findBySupplierId(String supplierId) {
        return jpaRepository.findBySupplierId(supplierId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void save(SupplierLifecycleSaga saga) {
        SupplierLifecycleSagaEntity entity = mapper.toEntity(saga);
        jpaRepository.save(entity);
        saga.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
