package com.homebase.ecom.onboarding.infrastructure.persistence.mapper;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;

public class OnboardingSagaMapper {

    public OnboardingSaga toModel(OnboardingSagaEntity entity) {
        if (entity == null) return null;
        OnboardingSaga model = new OnboardingSaga();
        model.setId(entity.getId());
        model.setSupplierId(entity.getSupplierId());
        model.setSupplierName(entity.getSupplierName());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setUpiId(entity.getUpiId());
        model.setAddress(entity.getAddress());
        model.setCommissionPercentage(entity.getCommissionPercentage());
        model.setDescription(entity.getDescription());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        // STM state
        model.setCurrentState(entity.getCurrentState());
        return model;
    }

    public OnboardingSagaEntity toEntity(OnboardingSaga model) {
        if (model == null) return null;
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId(model.getId());
        entity.setSupplierId(model.getSupplierId());
        entity.setSupplierName(model.getSupplierName());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setUpiId(model.getUpiId());
        entity.setAddress(model.getAddress());
        entity.setCommissionPercentage(model.getCommissionPercentage());
        entity.setDescription(model.getDescription());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        // STM state
        entity.setCurrentState(model.getCurrentState());
        return entity;
    }
}
