package com.homebase.ecom.returnrequest.infrastructure.persistence.mapper;

import java.util.stream.Collectors;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.model.ReturnrequestActivityLog;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestActivityLogEntity;
import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import org.chenile.workflow.activities.model.ActivityLog;

public class ReturnrequestMapper {

    public Returnrequest toModel(ReturnrequestEntity entity) {
        if (entity == null) return null;
        Returnrequest model = new Returnrequest();
        model.setId(entity.getId());
        model.orderId = entity.getOrderId();
        model.orderItemId = entity.getOrderItemId();
        model.reason = entity.getReason();
        model.quantity = entity.getQuantity();
        model.refundAmount = entity.getRefundAmount();
        model.returnType = entity.getReturnType();
        model.description = entity.getDescription();
        model.itemPrice = entity.getItemPrice();
        model.orderDeliveryDate = entity.getOrderDeliveryDate();
        model.inspectorId = entity.getInspectorId();
        model.inspectorNotes = entity.getInspectorNotes();
        model.rejectionReason = entity.getRejectionReason();
        model.rejectionComment = entity.getRejectionComment();
        model.pickupTrackingNumber = entity.getPickupTrackingNumber();
        model.warehouseId = entity.getWarehouseId();
        model.conditionOnReceipt = entity.getConditionOnReceipt();
        model.refundMethod = entity.getRefundMethod();
        model.refundTransactionId = entity.getRefundTransactionId();
        model.refundProcessedAt = entity.getRefundProcessedAt();
        model.setCurrentState(entity.getCurrentState());

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
        entity.setOrderItemId(model.orderItemId);
        entity.setReason(model.reason);
        entity.setQuantity(model.quantity);
        entity.setRefundAmount(model.refundAmount);
        entity.setReturnType(model.returnType);
        entity.setDescription(model.description);
        entity.setItemPrice(model.itemPrice);
        entity.setOrderDeliveryDate(model.orderDeliveryDate);
        entity.setInspectorId(model.inspectorId);
        entity.setInspectorNotes(model.inspectorNotes);
        entity.setRejectionReason(model.rejectionReason);
        entity.setRejectionComment(model.rejectionComment);
        entity.setPickupTrackingNumber(model.pickupTrackingNumber);
        entity.setWarehouseId(model.warehouseId);
        entity.setConditionOnReceipt(model.conditionOnReceipt);
        entity.setRefundMethod(model.refundMethod);
        entity.setRefundTransactionId(model.refundTransactionId);
        entity.setRefundProcessedAt(model.refundProcessedAt);
        entity.setCurrentState(model.getCurrentState());

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
}
