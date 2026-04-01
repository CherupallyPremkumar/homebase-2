package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for confirming delivery (SENDING -> SENT or SENT -> DELIVERED).
 */
public class MarkDeliveredNotificationPayload extends MinimalPayload {
    private String deliveryConfirmationId;

    public String getDeliveryConfirmationId() { return deliveryConfirmationId; }
    public void setDeliveryConfirmationId(String deliveryConfirmationId) { this.deliveryConfirmationId = deliveryConfirmationId; }
}
