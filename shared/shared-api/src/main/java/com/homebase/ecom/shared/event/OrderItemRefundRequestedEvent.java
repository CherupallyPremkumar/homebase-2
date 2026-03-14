package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a refund is requested for a specific item in an order.
 */
public class OrderItemRefundRequestedEvent implements Serializable {
    private String orderId;
    private String orderItemId;
    private BigDecimal refundAmount;
    private String currency;
    private String reason;
    private LocalDateTime timestamp;

    public OrderItemRefundRequestedEvent() {
    }

    public OrderItemRefundRequestedEvent(String orderId, String orderItemId, BigDecimal refundAmount, String currency,
            String reason) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.refundAmount = refundAmount;
        this.currency = currency;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
