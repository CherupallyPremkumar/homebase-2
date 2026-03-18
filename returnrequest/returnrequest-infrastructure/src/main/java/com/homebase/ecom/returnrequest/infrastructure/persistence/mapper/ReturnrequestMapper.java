package com.homebase.ecom.returnrequest.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.returnrequest.model.ReturnItem;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.model.ReturnrequestActivityLog;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestActivityLogEntity;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReturnrequestMapper {

    private static final Logger log = LoggerFactory.getLogger(ReturnrequestMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Returnrequest toModel(ReturnrequestEntity entity) {
        if (entity == null) return null;
        Returnrequest model = new Returnrequest();
        model.setId(entity.getId());
        model.orderId = entity.getOrderId();
        model.customerId = entity.getCustomerId();
        model.items = deserializeItems(entity.getItemsJson());
        model.reason = entity.getReason();
        model.returnType = entity.getReturnType();
        model.totalRefundAmount = entity.getTotalRefundAmount();
        model.restockingFee = entity.getRestockingFee();
        model.description = entity.getDescription();
        model.reviewerId = entity.getReviewerId();
        model.reviewNotes = entity.getReviewNotes();
        model.rejectionReason = entity.getRejectionReason();
        model.rejectionComment = entity.getRejectionComment();
        model.warehouseId = entity.getWarehouseId();
        model.conditionOnReceipt = entity.getConditionOnReceipt();
        model.inspectorId = entity.getInspectorId();
        model.inspectorNotes = entity.getInspectorNotes();
        model.orderDeliveryDate = entity.getOrderDeliveryDate();
        model.orderTotalValue = entity.getOrderTotalValue();
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);

        if (entity.getActivities() != null) {
            for (ReturnrequestActivityLogEntity actEntity : entity.getActivities()) {
                model.addActivity(actEntity.getName(), actEntity.getComment());
            }
        }

        return model;
    }

    public ReturnrequestEntity toEntity(Returnrequest model) {
        if (model == null) return null;
        ReturnrequestEntity entity = new ReturnrequestEntity();
        entity.setId(model.getId());
        entity.setOrderId(model.orderId);
        entity.setCustomerId(model.customerId);
        entity.setItemsJson(serializeItems(model.items));
        entity.setReason(model.reason);
        entity.setReturnType(model.returnType);
        entity.setTotalRefundAmount(model.totalRefundAmount);
        entity.setRestockingFee(model.restockingFee);
        entity.setDescription(model.description);
        entity.setReviewerId(model.reviewerId);
        entity.setReviewNotes(model.reviewNotes);
        entity.setRejectionReason(model.rejectionReason);
        entity.setRejectionComment(model.rejectionComment);
        entity.setWarehouseId(model.warehouseId);
        entity.setConditionOnReceipt(model.conditionOnReceipt);
        entity.setInspectorId(model.inspectorId);
        entity.setInspectorNotes(model.inspectorNotes);
        entity.setOrderDeliveryDate(model.orderDeliveryDate);
        entity.setOrderTotalValue(model.orderTotalValue);
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    private ReturnrequestActivityLogEntity toActivityEntity(ActivityLog activityLog) {
        ReturnrequestActivityLogEntity entity = new ReturnrequestActivityLogEntity();
        entity.setEventId(activityLog.getName());
        entity.setComment(activityLog.getComment());
        entity.setSuccess(activityLog.getSuccess());
        return entity;
    }

    private String serializeItems(List<ReturnItem> items) {
        if (items == null || items.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize return items", e);
            return null;
        }
    }

    private List<ReturnItem> deserializeItems(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<ReturnItem>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize return items", e);
            return new ArrayList<>();
        }
    }
}
