package com.homebase.ecom.shared.model.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when payment fails at gateway.
 */
public class PaymentFailedEvent implements Serializable {

    public static final String EVENT_TYPE = "PAYMENT_FAILED";

    private String orderId;

    /**
     * Gateway transaction id (Stripe payment_intent, Razorpay order/payment id, etc).
     * Kept separate from orderId for tracing.
     */
    private String gatewayTransactionId;

    private String eventType = EVENT_TYPE;
    private String failureReason;
    private LocalDateTime timestamp = LocalDateTime.now();

    public PaymentFailedEvent() {
    }

    public PaymentFailedEvent(String orderId, String gatewayTransactionId, String failureReason, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.failureReason = failureReason;
        this.timestamp = timestamp;
        this.eventType = EVENT_TYPE;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
