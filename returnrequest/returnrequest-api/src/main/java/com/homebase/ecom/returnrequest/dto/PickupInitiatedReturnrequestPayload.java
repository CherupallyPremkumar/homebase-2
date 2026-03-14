package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the pickupInitiated event.
 * Carries tracking number for return pickup.
 */
public class PickupInitiatedReturnrequestPayload extends MinimalPayload {

    private String pickupTrackingNumber;

    public String getPickupTrackingNumber() { return pickupTrackingNumber; }
    public void setPickupTrackingNumber(String pickupTrackingNumber) { this.pickupTrackingNumber = pickupTrackingNumber; }
}
