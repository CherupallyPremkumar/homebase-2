package com.homebase.ecom.order.model;

import java.io.Serializable;

/**
 * Payload for requesting cancellation of a specific item in an order.
 */
public class CancelOrderItemPayload implements Serializable {
    private String orderItemId;
    private String reason;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
