package com.homebase.ecom.supplierlifecycle.infrastructure.persistence;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.adapter.SupplierLifecycleSagaJpaRepository;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaEntity;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.mapper.SupplierLifecycleSagaMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileSupplierLifecycleSagaEntityStore
        extends ChenileJpaEntityStore<SupplierLifecycleSaga, SupplierLifecycleSagaEntity> {

    public ChenileSupplierLifecycleSagaEntityStore(SupplierLifecycleSagaJpaRepository repository,
                                                     SupplierLifecycleSagaMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
