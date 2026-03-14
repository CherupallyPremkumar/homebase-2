package com.homebase.ecom.notification.infrastructure.persistence.mapper;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;

public class NotificationMapper {

    public Notification toModel(NotificationEntity entity) {
        if (entity == null) return null;
        Notification model = new Notification();
        model.setId(entity.getId());
        model.setUserId(entity.getUserId());
        model.setChannel(entity.getChannel());
        model.setTemplateCode(entity.getTemplateCode());
        model.setSubject(entity.getSubject());
        model.setBody(entity.getBody());
        model.setReferenceType(entity.getReferenceType());
        model.setReferenceId(entity.getReferenceId());
        model.setReadAt(entity.getReadAt());
        model.setSentAt(entity.getSentAt());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        // For STM state
        model.setCurrentState(entity.getCurrentState());
        return model;
    }

    public NotificationEntity toEntity(Notification model) {
        if (model == null) return null;
        NotificationEntity entity = new NotificationEntity();
        entity.setId(model.getId());
        entity.setUserId(model.getUserId());
        entity.setChannel(model.getChannel());
        entity.setTemplateCode(model.getTemplateCode());
        entity.setSubject(model.getSubject());
        entity.setBody(model.getBody());
        entity.setReferenceType(model.getReferenceType());
        entity.setReferenceId(model.getReferenceId());
        entity.setReadAt(model.getReadAt());
        entity.setSentAt(model.getSentAt());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        // For STM state
        entity.setCurrentState(model.getCurrentState());
        return entity;
    }
}
