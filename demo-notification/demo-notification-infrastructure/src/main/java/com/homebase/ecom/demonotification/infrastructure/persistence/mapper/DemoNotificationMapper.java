package com.homebase.ecom.demonotification.infrastructure.persistence.mapper;

import com.homebase.ecom.demonotification.model.DemoNotification;
import com.homebase.ecom.demonotification.infrastructure.persistence.entity.DemoNotificationEntity;

/**
 * Bidirectional mapper between DemoNotification domain model and DemoNotificationEntity JPA entity.
 */
public class DemoNotificationMapper {

    public DemoNotification toModel(DemoNotificationEntity entity) {
        if (entity == null) return null;
        DemoNotification model = new DemoNotification();

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
        model.setOrderId(entity.getOrderId());
        model.setMessage(entity.getMessage());
        model.setChannel(entity.getChannel());

        return model;
    }

    public DemoNotificationEntity toEntity(DemoNotification model) {
        if (model == null) return null;
        DemoNotificationEntity entity = new DemoNotificationEntity();

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
        entity.setOrderId(model.getOrderId());
        entity.setMessage(model.getMessage());
        entity.setChannel(model.getChannel());

        return entity;
    }
}
