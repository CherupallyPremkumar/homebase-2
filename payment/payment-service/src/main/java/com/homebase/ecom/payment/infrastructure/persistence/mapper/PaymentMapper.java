package com.homebase.ecom.payment.infrastructure.persistence.mapper;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.model.PaymentActivityLogEntry;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentActivityLogEntity;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Payment domain model and PaymentEntity JPA entity.
 */
public class PaymentMapper {

    public Payment toModel(PaymentEntity entity) {
        if (entity == null) return null;
        Payment model = new Payment();
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setCustomerId(entity.getCustomerId());
        model.setAmount(entity.getAmount());
        model.setCurrency(entity.getCurrency());
        model.setPaymentMethod(entity.getPaymentMethod());
        model.setGatewayTransactionId(entity.getGatewayTransactionId());
        model.setGatewayResponse(entity.getGatewayResponse());
        model.setRetryCount(entity.getRetryCount());
        model.setFailureReason(entity.getFailureReason());
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);
        if (entity.getCreatedTime() != null) model.setCreatedTime(entity.getCreatedTime());
        if (entity.getLastModifiedTime() != null) model.setLastModifiedTime(entity.getLastModifiedTime());

        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                    .map(this::toActivityModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }

    public PaymentEntity toEntity(Payment model) {
        if (model == null) return null;
        PaymentEntity entity = new PaymentEntity();
        entity.setId(model.getId());
        entity.setOrderId(model.getOrderId());
        entity.setCustomerId(model.getCustomerId());
        entity.setAmount(model.getAmount());
        entity.setCurrency(model.getCurrency());
        entity.setPaymentMethod(model.getPaymentMethod());
        entity.setGatewayTransactionId(model.getGatewayTransactionId());
        entity.setGatewayResponse(model.getGatewayResponse());
        entity.setRetryCount(model.getRetryCount());
        entity.setFailureReason(model.getFailureReason());
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private ActivityLog toActivityModel(PaymentActivityLogEntity entity) {
        PaymentActivityLogEntry log = new PaymentActivityLogEntry();
        log.activityName = entity.getName();
        log.activityComment = entity.getComment();
        log.activitySuccess = entity.getSuccess() != null && entity.getSuccess();
        return log;
    }

    private PaymentActivityLogEntity toActivityEntity(ActivityLog model) {
        PaymentActivityLogEntity entity = new PaymentActivityLogEntity();
        entity.setEventId(model.getName());
        entity.setComment(model.getComment());
        entity.setSuccess(model.getSuccess());
        return entity;
    }
}
