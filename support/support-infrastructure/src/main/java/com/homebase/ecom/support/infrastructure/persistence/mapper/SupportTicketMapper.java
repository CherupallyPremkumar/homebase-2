package com.homebase.ecom.support.infrastructure.persistence.mapper;

import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.SupportTicketActivityLog;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketActivityLogEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.TicketMessageEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SupportTicketMapper {

    public SupportTicket toModel(SupportTicketEntity entity) {
        if (entity == null) return null;
        SupportTicket model = new SupportTicket();
        model.setId(entity.getId());
        model.setUserId(entity.getUserId());
        model.setOrderId(entity.getOrderId());
        model.setSubject(entity.getSubject());
        model.setCategory(entity.getCategory());
        model.setPriority(entity.getPriority());
        model.setAssignedTo(entity.getAssignedTo());
        model.setResolvedAt(entity.getResolvedAt());
        model.setDescription(entity.getDescription());
        model.setCurrentState(entity.getCurrentState());

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
        entity.setUserId(model.getUserId());
        entity.setOrderId(model.getOrderId());
        entity.setSubject(model.getSubject());
        entity.setCategory(model.getCategory());
        entity.setPriority(model.getPriority());
        entity.setAssignedTo(model.getAssignedTo());
        entity.setResolvedAt(model.getResolvedAt());
        entity.setDescription(model.getDescription());
        entity.setCurrentState(model.getCurrentState());

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
        model.setAttachments(entity.getAttachments());
        return model;
    }

    public TicketMessageEntity toMessageEntity(TicketMessage model) {
        if (model == null) return null;
        TicketMessageEntity entity = new TicketMessageEntity();
        entity.setId(model.getId());
        entity.setSenderId(model.getSenderId());
        entity.setSenderType(model.getSenderType());
        entity.setMessage(model.getMessage());
        entity.setAttachments(model.getAttachments());
        return entity;
    }
}
