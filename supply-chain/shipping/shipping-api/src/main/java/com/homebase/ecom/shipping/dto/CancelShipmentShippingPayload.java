package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the cancelShipment event: PENDING/LABEL_CREATED -> CANCELLED.
 * Contains cancellation reason.
 */
public class CancelShipmentShippingPayload extends MinimalPayload {

    private String cancellationReason;

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
}
