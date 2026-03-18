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
        model.setId(entity.getId());
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
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);

        if (entity.getMessages() != null) {
            model.setMessages(entity.getMessages().stream()
                    .map(this::toMessageModel)
                    .collect(Collectors.toList()));
        }

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
        entity.setId(model.getId());
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
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        if (model.getMessages() != null) {
            entity.setMessages(model.getMessages().stream()
                    .map(this::toMessageEntity)
                    .collect(Collectors.toList()));
        }

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
        // Simple JSON array parse: ["a","b"] -> List
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
