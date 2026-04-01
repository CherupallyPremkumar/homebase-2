package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for markDelivered event.
 */
public class MarkDeliveredOrderPayload extends MinimalPayload {
    private String deliveryConfirmation;

    public String getDeliveryConfirmation() { return deliveryConfirmation; }
    public void setDeliveryConfirmation(String deliveryConfirmation) { this.deliveryConfirmation = deliveryConfirmation; }
}
