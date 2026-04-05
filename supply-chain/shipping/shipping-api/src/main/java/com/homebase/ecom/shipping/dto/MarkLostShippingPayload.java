package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the markLost event: IN_TRANSIT -> LOST.
 * Package lost in transit, triggers insurance claim.
 */
public class MarkLostShippingPayload extends MinimalPayload {

    private String lostReason;
    private String lastKnownLocation;

    public String getLostReason() { return lostReason; }
    public void setLostReason(String lostReason) { this.lostReason = lostReason; }

    public String getLastKnownLocation() { return lastKnownLocation; }
    public void setLastKnownLocation(String lastKnownLocation) { this.lastKnownLocation = lastKnownLocation; }
}
