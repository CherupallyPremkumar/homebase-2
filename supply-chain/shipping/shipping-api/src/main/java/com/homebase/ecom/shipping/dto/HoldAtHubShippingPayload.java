package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the holdAtHub event: IN_TRANSIT -> HELD_AT_HUB.
 * Package held at delivery hub or locker for customer pickup.
 */
public class HoldAtHubShippingPayload extends MinimalPayload {

    private String hubName;
    private String holdReason;

    public String getHubName() { return hubName; }
    public void setHubName(String hubName) { this.hubName = hubName; }

    public String getHoldReason() { return holdReason; }
    public void setHoldReason(String holdReason) { this.holdReason = holdReason; }
}
