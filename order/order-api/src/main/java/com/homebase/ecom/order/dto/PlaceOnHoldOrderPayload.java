package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for placeOnHold event -- admin places order on hold.
 */
public class PlaceOnHoldOrderPayload extends MinimalPayload {
    private String holdReason;

    public String getHoldReason() { return holdReason; }
    public void setHoldReason(String holdReason) { this.holdReason = holdReason; }
}
