package com.homebase.ecom.promo.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for incrementing usage count on a promo.
 * Triggered by order.events when an ORDER_COMPLETED event is received.
 */
public class IncrementUsagePayload extends MinimalPayload {
    private String orderId;
    private String customerId;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
