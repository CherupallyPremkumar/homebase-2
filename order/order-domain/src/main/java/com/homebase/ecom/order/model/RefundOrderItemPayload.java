package com.homebase.ecom.order.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Payload for requesting a refund for a specific item in an order.
 */
public class RefundOrderItemPayload implements Serializable {
    private String orderItemId;
    private BigDecimal amount;
    private String reason;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
