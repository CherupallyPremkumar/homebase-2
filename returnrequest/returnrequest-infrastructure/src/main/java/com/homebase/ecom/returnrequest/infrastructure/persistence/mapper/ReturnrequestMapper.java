package com.homebase.ecom.returnrequest.infrastructure.persistence.mapper;

import java.util.ArrayList;
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

        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Tenant
        model.setTenant(entity.tenant);

        // Domain fields
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

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (ReturnrequestActivityLogEntity actEntity : entity.getActivities()) {
                ReturnrequestActivityLog actLog = new ReturnrequestActivityLog();
                actLog.activityName = actEntity.getName();
                actLog.activityComment = actEntity.getComment();
                actLog.activitySuccess = actEntity.getSuccess();
                activityLogs.add(actLog);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public ReturnrequestEntity toEntity(Returnrequest model) {
        if (model == null) return null;
        ReturnrequestEntity entity = new ReturnrequestEntity();

        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Tenant
        entity.tenant = model.getTenant();

        // Domain fields
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

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    /**
     * Merges updated fields from a new entity into an existing managed JPA entity.
     * Used by ChenileJpaEntityStore to preserve optimistic locking via @Version.
     */
    public void mergeEntity(ReturnrequestEntity existing, ReturnrequestEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Domain fields
        existing.setOrderId(updated.getOrderId());
        existing.setCustomerId(updated.getCustomerId());
        existing.setItemsJson(updated.getItemsJson());
        existing.setReason(updated.getReason());
        existing.setReturnType(updated.getReturnType());
        existing.setTotalRefundAmount(updated.getTotalRefundAmount());
        existing.setRestockingFee(updated.getRestockingFee());
        existing.setDescription(updated.getDescription());
        existing.setReviewerId(updated.getReviewerId());
        existing.setReviewNotes(updated.getReviewNotes());
        existing.setRejectionReason(updated.getRejectionReason());
        existing.setRejectionComment(updated.getRejectionComment());
        existing.setWarehouseId(updated.getWarehouseId());
        existing.setConditionOnReceipt(updated.getConditionOnReceipt());
        existing.setInspectorId(updated.getInspectorId());
        existing.setInspectorNotes(updated.getInspectorNotes());
        existing.setOrderDeliveryDate(updated.getOrderDeliveryDate());
        existing.setOrderTotalValue(updated.getOrderTotalValue());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- replace collection contents
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
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
