package com.homebase.ecom.notification.infrastructure.persistence.mapper;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;

import java.util.HashMap;

/**
 * Bidirectional mapper between Notification domain model and NotificationEntity JPA entity.
 */
public class NotificationMapper {

    public Notification toModel(NotificationEntity entity) {
        if (entity == null) return null;
        Notification model = new Notification();
        model.setId(entity.getId());
        model.setCustomerId(entity.getCustomerId());
        model.setChannel(entity.getChannel());
        model.setTemplateId(entity.getTemplateId());
        model.setSubject(entity.getSubject());
        model.setBody(entity.getBody());
        model.setRecipientAddress(entity.getRecipientAddress());
        model.setMetadata(entity.getMetadata() != null ? new HashMap<>(entity.getMetadata()) : new HashMap<>());
        model.setSentAt(entity.getSentAt());
        model.setDeliveredAt(entity.getDeliveredAt());
        model.setFailureReason(entity.getFailureReason());
        model.setRetryCount(entity.getRetryCount());
        // For STM state
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);
        return model;
    }

    public NotificationEntity toEntity(Notification model) {
        if (model == null) return null;
        NotificationEntity entity = new NotificationEntity();
        entity.setId(model.getId());
        entity.setCustomerId(model.getCustomerId());
        entity.setChannel(model.getChannel());
        entity.setTemplateId(model.getTemplateId());
        entity.setSubject(model.getSubject());
        entity.setBody(model.getBody());
        entity.setRecipientAddress(model.getRecipientAddress());
        entity.setMetadata(model.getMetadata() != null ? new HashMap<>(model.getMetadata()) : new HashMap<>());
        entity.setSentAt(model.getSentAt());
        entity.setDeliveredAt(model.getDeliveredAt());
        entity.setFailureReason(model.getFailureReason());
        entity.setRetryCount(model.getRetryCount());
        // For STM state
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();
        return entity;
    }
}
