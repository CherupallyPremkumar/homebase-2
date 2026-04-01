package com.homebase.ecom.support.infrastructure.persistence.mapper;

import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.SupportTicketActivityLog;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketActivityLogEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.TicketMessageEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SupportTicketMapper {

    public SupportTicket toModel(SupportTicketEntity entity) {
        if (entity == null) return null;
        SupportTicket model = new SupportTicket();

        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setVersion(entity.getVersion());
        model.setTenant(entity.tenant);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());

        // Business fields
        model.setCustomerId(entity.getCustomerId());
        model.setOrderId(entity.getOrderId());
        model.setSubject(entity.getSubject());
        model.setCategory(entity.getCategory());
        model.setPriority(entity.getPriority());
        model.setDescription(entity.getDescription());
        model.setAssignedAgentId(entity.getAssignedAgentId());
        model.setResolvedAt(entity.getResolvedAt());
        model.setReopenCount(entity.getReopenCount());
        model.setSlaBreached(entity.isSlaBreached());
        model.setAutoCloseReady(entity.isAutoCloseReady());

        // Changeset support-004 fields
        model.setChannel(entity.getChannel());
        model.setRelatedEntityType(entity.getRelatedEntityType());
        model.setRelatedEntityId(entity.getRelatedEntityId());
        model.setSatisfactionRating(entity.getSatisfactionRating());
        model.setResolutionNotes(entity.getResolutionNotes());
        model.setEscalated(entity.isEscalated());
        model.setEscalationReason(entity.getEscalationReason());

        // Messages
        if (entity.getMessages() != null) {
            model.setMessages(entity.getMessages().stream()
                    .map(this::toMessageModel)
                    .collect(Collectors.toList()));
        }

        // Activities
        if (entity.getActivities() != null) {
            for (SupportTicketActivityLogEntity actEntity : entity.getActivities()) {
                model.addActivity(actEntity.getName(), actEntity.getComment());
            }
        }

        return model;
    }

    public SupportTicketEntity toEntity(SupportTicket model) {
        if (model == null) return null;
        SupportTicketEntity entity = new SupportTicketEntity();

        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);
        entity.tenant = model.getTenant();

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());

        // Business fields
        entity.setCustomerId(model.getCustomerId());
        entity.setOrderId(model.getOrderId());
        entity.setSubject(model.getSubject());
        entity.setCategory(model.getCategory());
        entity.setPriority(model.getPriority());
        entity.setDescription(model.getDescription());
        entity.setAssignedAgentId(model.getAssignedAgentId());
        entity.setResolvedAt(model.getResolvedAt());
        entity.setReopenCount(model.getReopenCount());
        entity.setSlaBreached(model.isSlaBreached());
        entity.setAutoCloseReady(model.isAutoCloseReady());

        // Changeset support-004 fields
        entity.setChannel(model.getChannel());
        entity.setRelatedEntityType(model.getRelatedEntityType());
        entity.setRelatedEntityId(model.getRelatedEntityId());
        entity.setSatisfactionRating(model.getSatisfactionRating());
        entity.setResolutionNotes(model.getResolutionNotes());
        entity.setEscalated(model.isEscalated());
        entity.setEscalationReason(model.getEscalationReason());

        // Messages
        if (model.getMessages() != null) {
            entity.setMessages(model.getMessages().stream()
                    .map(this::toMessageEntity)
                    .collect(Collectors.toList()));
        }

        // Activities
        if (model.obtainActivities() != null) {
            ArrayList<SupportTicketActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog act : model.obtainActivities()) {
                SupportTicketActivityLogEntity actEntity = new SupportTicketActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }

    /**
     * Merges incoming domain model fields onto an existing JPA entity.
     * Used by EntityStore during STM transitions to preserve JPA-managed state
     * (managed collections, version) while applying domain changes.
     */
    public SupportTicketEntity mergeEntity(SupportTicket model, SupportTicketEntity existing) {
        if (model == null) return existing;
        if (existing == null) return toEntity(model);

        // STM state fields
        existing.setCurrentState(model.getCurrentState());
        existing.setStateEntryTime(model.getStateEntryTime());

        // Base entity fields that may change
        existing.setLastModifiedTime(model.getLastModifiedTime() != null ? model.getLastModifiedTime() : new Date());
        existing.setLastModifiedBy(model.getLastModifiedBy());

        // Business fields
        existing.setCustomerId(model.getCustomerId());
        existing.setOrderId(model.getOrderId());
        existing.setSubject(model.getSubject());
        existing.setCategory(model.getCategory());
        existing.setPriority(model.getPriority());
        existing.setDescription(model.getDescription());
        existing.setAssignedAgentId(model.getAssignedAgentId());
        existing.setResolvedAt(model.getResolvedAt());
        existing.setReopenCount(model.getReopenCount());
        existing.setSlaBreached(model.isSlaBreached());
        existing.setAutoCloseReady(model.isAutoCloseReady());

        // Changeset support-004 fields
        existing.setChannel(model.getChannel());
        existing.setRelatedEntityType(model.getRelatedEntityType());
        existing.setRelatedEntityId(model.getRelatedEntityId());
        existing.setSatisfactionRating(model.getSatisfactionRating());
        existing.setResolutionNotes(model.getResolutionNotes());
        existing.setEscalated(model.isEscalated());
        existing.setEscalationReason(model.getEscalationReason());

        // Messages -- replace managed collection contents
        existing.getMessages().clear();
        if (model.getMessages() != null) {
            for (TicketMessage msg : model.getMessages()) {
                existing.getMessages().add(toMessageEntity(msg));
            }
        }

        // Activities -- replace managed collection contents
        existing.getActivities().clear();
        if (model.obtainActivities() != null) {
            for (ActivityLog act : model.obtainActivities()) {
                SupportTicketActivityLogEntity actEntity = new SupportTicketActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                existing.getActivities().add(actEntity);
            }
        }

        return existing;
    }

    public TicketMessage toMessageModel(TicketMessageEntity entity) {
        if (entity == null) return null;
        TicketMessage model = new TicketMessage();
        model.setId(entity.getId());
        model.setSenderId(entity.getSenderId());
        model.setSenderType(entity.getSenderType());
        model.setMessage(entity.getMessage());
        model.setTimestamp(entity.getTimestamp());
        if (entity.getAttachments() != null) {
            model.setAttachments(parseAttachments(entity.getAttachments()));
        }
        return model;
    }

    public TicketMessageEntity toMessageEntity(TicketMessage model) {
        if (model == null) return null;
        TicketMessageEntity entity = new TicketMessageEntity();
        entity.setId(model.getId());
        entity.setSenderId(model.getSenderId());
        entity.setSenderType(model.getSenderType());
        entity.setMessage(model.getMessage());
        entity.setTimestamp(model.getTimestamp() != null ? model.getTimestamp() : new Date());
        if (model.getAttachments() != null) {
            entity.setAttachments(serializeAttachments(model.getAttachments()));
        }
        return entity;
    }

    private List<String> parseAttachments(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            List<String> result = new ArrayList<>();
            String stripped = json.replace("[", "").replace("]", "").replace("\"", "");
            if (!stripped.isBlank()) {
                for (String s : stripped.split(",")) {
                    result.add(s.trim());
                }
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String serializeAttachments(List<String> attachments) {
        if (attachments == null || attachments.isEmpty()) return "[]";
        return "[" + attachments.stream()
                .map(a -> "\"" + a + "\"")
                .collect(Collectors.joining(",")) + "]";
    }
}
