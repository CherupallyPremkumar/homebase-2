package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for deliveryFailed event -- all delivery attempts exhausted.
 */
public class DeliveryFailedOrderPayload extends MinimalPayload {
    private String failureReason;
    private int deliveryAttempts;

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public int getDeliveryAttempts() { return deliveryAttempts; }
    public void setDeliveryAttempts(int deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; }
}
