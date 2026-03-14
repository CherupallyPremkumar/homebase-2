package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a refund is fully processed.
 */
public class OrderRefundedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private String refundTransactionId;
    private BigDecimal refundAmount;
    private String currency;
    private LocalDateTime refundedAt;

    public OrderRefundedEvent() {
    }

    public OrderRefundedEvent(String orderId, String customerId, String refundTransactionId,
            BigDecimal refundAmount, String currency, LocalDateTime refundedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.refundTransactionId = refundTransactionId;
        this.refundAmount = refundAmount;
        this.currency = currency;
        this.refundedAt = refundedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(String refundTransactionId) { this.refundTransactionId = refundTransactionId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getRefundedAt() { return refundedAt; }
    public void setRefundedAt(LocalDateTime refundedAt) { this.refundedAt = refundedAt; }
}
