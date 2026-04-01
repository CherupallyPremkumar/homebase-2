package com.homebase.ecom.notification.infrastructure.persistence.mapper;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.model.NotificationActivityLog;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationActivityLogEntity;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Notification domain model and NotificationEntity JPA entity.
 * Maps ALL fields including BaseEntity/STM fields, changeset 006 columns, activities, and metadata.
 */
public class NotificationMapper {

    public Notification toModel(NotificationEntity entity) {
        if (entity == null) return null;
        Notification model = new Notification();

        // BaseEntity fields (id, timestamps, audit, version)
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setVersion(entity.getVersion());

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Tenant
        model.setTenant(entity.tenant);

        // Notification-specific fields
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

        // Changeset 006 fields
        model.setReferenceType(entity.getReferenceType());
        model.setReferenceId(entity.getReferenceId());
        model.setPriority(entity.getPriority());
        model.setScheduledAt(entity.getScheduledAt());
        model.setOpenedAt(entity.getOpenedAt());
        model.setClickedAt(entity.getClickedAt());

        // Activities
        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                    .map(this::toActivityModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }

    public NotificationEntity toEntity(Notification model) {
        if (model == null) return null;
        NotificationEntity entity = new NotificationEntity();

        // BaseEntity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Tenant
        entity.tenant = model.getTenant();

        // Notification-specific fields
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

        // Changeset 006 fields
        entity.setReferenceType(model.getReferenceType());
        entity.setReferenceId(model.getReferenceId());
        entity.setPriority(model.getPriority());
        entity.setScheduledAt(model.getScheduledAt());
        entity.setOpenedAt(model.getOpenedAt());
        entity.setClickedAt(model.getClickedAt());

        // Activities
        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    /**
     * Merges fields from updated entity into existing (DB-loaded) entity.
     * Preserves @Version and JPA-managed fields on existing entity.
     */
    public void mergeEntity(NotificationEntity existing, NotificationEntity updated) {
        // STM state
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Notification-specific fields
        existing.setCustomerId(updated.getCustomerId());
        existing.setChannel(updated.getChannel());
        existing.setTemplateId(updated.getTemplateId());
        existing.setSubject(updated.getSubject());
        existing.setBody(updated.getBody());
        existing.setRecipientAddress(updated.getRecipientAddress());
        existing.setMetadata(updated.getMetadata() != null ? new HashMap<>(updated.getMetadata()) : new HashMap<>());
        existing.setSentAt(updated.getSentAt());
        existing.setDeliveredAt(updated.getDeliveredAt());
        existing.setFailureReason(updated.getFailureReason());
        existing.setRetryCount(updated.getRetryCount());

        // Changeset 006 fields
        existing.setReferenceType(updated.getReferenceType());
        existing.setReferenceId(updated.getReferenceId());
        existing.setPriority(updated.getPriority());
        existing.setScheduledAt(updated.getScheduledAt());
        existing.setOpenedAt(updated.getOpenedAt());
        existing.setClickedAt(updated.getClickedAt());

        // Merge activities: match by ID, preserve versions on existing
        Map<String, NotificationActivityLogEntity> existingActivitiesById = existing.getActivities().stream()
                .filter(a -> a.getId() != null)
                .collect(Collectors.toMap(NotificationActivityLogEntity::getId, Function.identity()));

        List<NotificationActivityLogEntity> mergedActivities = new ArrayList<>();
        if (updated.getActivities() != null) {
            for (NotificationActivityLogEntity updatedAct : updated.getActivities()) {
                NotificationActivityLogEntity existingAct = updatedAct.getId() != null
                        ? existingActivitiesById.get(updatedAct.getId()) : null;
                if (existingAct != null) {
                    existingAct.setEventId(updatedAct.getName());
                    existingAct.setSuccess(updatedAct.getSuccess());
                    existingAct.setComment(updatedAct.getComment());
                    mergedActivities.add(existingAct);
                } else {
                    mergedActivities.add(updatedAct);
                }
            }
        }
        existing.getActivities().clear();
        existing.getActivities().addAll(mergedActivities);
    }

    // ── Activity mapping helpers ──────────────────────────────────────────

    private NotificationActivityLog toActivityModel(NotificationActivityLogEntity entity) {
        if (entity == null) return null;
        NotificationActivityLog model = new NotificationActivityLog();
        model.activityName = entity.getName();
        model.activitySuccess = entity.getSuccess();
        model.activityComment = entity.getComment();
        return model;
    }

    private NotificationActivityLogEntity toActivityEntity(NotificationActivityLog model) {
        if (model == null) return null;
        NotificationActivityLogEntity entity = new NotificationActivityLogEntity();
        entity.setEventId(model.getName());
        entity.setSuccess(model.getSuccess());
        entity.setComment(model.getComment());
        return entity;
    }
}
