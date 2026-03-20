package com.homebase.ecom.payment.infrastructure.persistence.mapper;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.model.PaymentActivityLogEntry;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentActivityLogEntity;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Payment domain model and PaymentEntity JPA entity.
 * Maps ALL columns from db-migrations payment schema.
 */
public class PaymentMapper {

    public Payment toModel(PaymentEntity entity) {
        if (entity == null) return null;
        Payment model = new Payment();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setTenant(entity.tenant);
        if (entity.getCreatedTime() != null) model.setCreatedTime(entity.getCreatedTime());
        if (entity.getLastModifiedTime() != null) model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        // STM state
        model.setCurrentState(entity.getCurrentState());

        // Core business fields
        model.setOrderId(entity.getOrderId());
        model.setCustomerId(entity.getCustomerId());
        model.setAmount(entity.getAmount());
        model.setCurrency(entity.getCurrency());
        model.setPaymentMethod(entity.getPaymentMethod());

        // Payment method details
        model.setPaymentMethodType(entity.getPaymentMethodType());
        model.setCardLastFour(entity.getCardLastFour());
        model.setCardBrand(entity.getCardBrand());
        model.setUpiId(entity.getUpiId());

        // Gateway fields
        model.setGatewayName(entity.getGatewayName());
        model.setGatewayOrderId(entity.getGatewayOrderId());
        model.setGatewayPaymentId(entity.getGatewayPaymentId());
        model.setGatewayTransactionId(entity.getGatewayTransactionId());
        model.setGatewayResponse(entity.getGatewayResponse());

        // Checkout / idempotency
        model.setCheckoutId(entity.getCheckoutId());
        model.setIdempotencyKey(entity.getIdempotencyKey());

        // Refund
        model.setRefundAmount(entity.getRefundAmount());
        model.setRefundReason(entity.getRefundReason());

        // Retry / failure
        model.setRetryCount(entity.getRetryCount());
        model.setFailureReason(entity.getFailureReason());

        // Activities
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

        // BaseEntity fields
        entity.setId(model.getId());
        entity.tenant = model.getTenant();
        if (model.getVersion() != null) entity.setVersion(model.getVersion());

        // STM state
        entity.setCurrentState(model.getCurrentState());

        // Core business fields
        entity.setOrderId(model.getOrderId());
        entity.setCustomerId(model.getCustomerId());
        entity.setAmount(model.getAmount());
        entity.setCurrency(model.getCurrency());
        entity.setPaymentMethod(model.getPaymentMethod());

        // Payment method details
        entity.setPaymentMethodType(model.getPaymentMethodType());
        entity.setCardLastFour(model.getCardLastFour());
        entity.setCardBrand(model.getCardBrand());
        entity.setUpiId(model.getUpiId());

        // Gateway fields
        entity.setGatewayName(model.getGatewayName());
        entity.setGatewayOrderId(model.getGatewayOrderId());
        entity.setGatewayPaymentId(model.getGatewayPaymentId());
        entity.setGatewayTransactionId(model.getGatewayTransactionId());
        entity.setGatewayResponse(model.getGatewayResponse());

        // Checkout / idempotency
        entity.setCheckoutId(model.getCheckoutId());
        entity.setIdempotencyKey(model.getIdempotencyKey());

        // Refund
        entity.setRefundAmount(model.getRefundAmount());
        entity.setRefundReason(model.getRefundReason());

        // Retry / failure
        entity.setRetryCount(model.getRetryCount());
        entity.setFailureReason(model.getFailureReason());

        // Activities
        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    /**
     * Merges incoming model fields onto an existing entity, preserving
     * fields the incoming model does not set (null-safe).
     * Used by EntityStore for STM processById operations.
     */
    public PaymentEntity mergeEntity(Payment incoming, PaymentEntity existing) {
        if (incoming == null) return existing;
        if (existing == null) return toEntity(incoming);

        // STM state — always update
        if (incoming.getCurrentState() != null) {
            existing.setCurrentState(incoming.getCurrentState());
        }

        // Core fields — update if provided
        if (incoming.getOrderId() != null) existing.setOrderId(incoming.getOrderId());
        if (incoming.getCustomerId() != null) existing.setCustomerId(incoming.getCustomerId());
        if (incoming.getAmount() != null) existing.setAmount(incoming.getAmount());
        if (incoming.getCurrency() != null) existing.setCurrency(incoming.getCurrency());
        if (incoming.getPaymentMethod() != null) existing.setPaymentMethod(incoming.getPaymentMethod());

        // Payment method details
        if (incoming.getPaymentMethodType() != null) existing.setPaymentMethodType(incoming.getPaymentMethodType());
        if (incoming.getCardLastFour() != null) existing.setCardLastFour(incoming.getCardLastFour());
        if (incoming.getCardBrand() != null) existing.setCardBrand(incoming.getCardBrand());
        if (incoming.getUpiId() != null) existing.setUpiId(incoming.getUpiId());

        // Gateway fields
        if (incoming.getGatewayName() != null) existing.setGatewayName(incoming.getGatewayName());
        if (incoming.getGatewayOrderId() != null) existing.setGatewayOrderId(incoming.getGatewayOrderId());
        if (incoming.getGatewayPaymentId() != null) existing.setGatewayPaymentId(incoming.getGatewayPaymentId());
        if (incoming.getGatewayTransactionId() != null) existing.setGatewayTransactionId(incoming.getGatewayTransactionId());
        if (incoming.getGatewayResponse() != null) existing.setGatewayResponse(incoming.getGatewayResponse());

        // Checkout / idempotency
        if (incoming.getCheckoutId() != null) existing.setCheckoutId(incoming.getCheckoutId());
        if (incoming.getIdempotencyKey() != null) existing.setIdempotencyKey(incoming.getIdempotencyKey());

        // Refund
        if (incoming.getRefundAmount() != null) existing.setRefundAmount(incoming.getRefundAmount());
        if (incoming.getRefundReason() != null) existing.setRefundReason(incoming.getRefundReason());

        // Retry / failure — always update (primitives)
        existing.setRetryCount(incoming.getRetryCount());
        // failureReason can be explicitly cleared (set to null)
        existing.setFailureReason(incoming.getFailureReason());

        // Version
        if (incoming.getVersion() != null) existing.setVersion(incoming.getVersion());

        // Activities
        if (incoming.getActivities() != null) {
            existing.setActivities(incoming.getActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        }

        return existing;
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
