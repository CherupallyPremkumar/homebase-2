package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.mapper;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaEntity;

public class SupplierLifecycleSagaMapper {

    public SupplierLifecycleSaga toModel(SupplierLifecycleSagaEntity entity) {
        if (entity == null) return null;
        SupplierLifecycleSaga model = new SupplierLifecycleSaga();
        model.setId(entity.getId());
        model.setSupplierId(entity.getSupplierId());
        model.setAction(entity.getAction());
        model.setReason(entity.getReason());
        model.setProductsAffected(entity.getProductsAffected());
        model.setCatalogEntriesRemoved(entity.getCatalogEntriesRemoved());
        model.setInventoryFrozen(entity.getInventoryFrozen());
        model.setOrdersCancelled(entity.getOrdersCancelled());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        model.setCurrentState(entity.getCurrentState());
        return model;
    }

    public SupplierLifecycleSagaEntity toEntity(SupplierLifecycleSaga model) {
        if (model == null) return null;
        SupplierLifecycleSagaEntity entity = new SupplierLifecycleSagaEntity();
        entity.setId(model.getId());
        entity.setSupplierId(model.getSupplierId());
        entity.setAction(model.getAction());
        entity.setReason(model.getReason());
        entity.setProductsAffected(model.getProductsAffected());
        entity.setCatalogEntriesRemoved(model.getCatalogEntriesRemoved());
        entity.setInventoryFrozen(model.getInventoryFrozen());
        entity.setOrdersCancelled(model.getOrdersCancelled());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        entity.setCurrentState(model.getCurrentState());
        return entity;
    }
}
