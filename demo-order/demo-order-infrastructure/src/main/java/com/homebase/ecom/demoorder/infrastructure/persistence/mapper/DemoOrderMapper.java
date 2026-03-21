package com.homebase.ecom.demoorder.infrastructure.persistence.mapper;

import com.homebase.ecom.demoorder.model.DemoOrder;
import com.homebase.ecom.demoorder.infrastructure.persistence.entity.DemoOrderEntity;

/**
 * Bidirectional mapper between DemoOrder domain model and DemoOrderEntity JPA entity.
 */
public class DemoOrderMapper {

    public DemoOrder toModel(DemoOrderEntity entity) {
        if (entity == null) return null;
        DemoOrder model = new DemoOrder();

        // Base entity fields
        model.setId(entity.getId());
        model.setVersion(entity.getVersion());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaLate(entity.getSlaLate());
        model.setSlaTendingLate(entity.getSlaTendingLate());

        // Business fields
        model.setProductName(entity.getProductName());
        model.setQuantity(entity.getQuantity());
        model.setCustomerId(entity.getCustomerId());

        return model;
    }

    public DemoOrderEntity toEntity(DemoOrder model) {
        if (model == null) return null;
        DemoOrderEntity entity = new DemoOrderEntity();

        // Base entity fields
        entity.setId(model.getId());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaLate(model.getSlaLate());
        entity.setSlaTendingLate(model.getSlaTendingLate());

        // Business fields
        entity.setProductName(model.getProductName());
        entity.setQuantity(model.getQuantity());
        entity.setCustomerId(model.getCustomerId());

        return entity;
    }
}
