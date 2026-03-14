package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the returnPickup event.
 * Contains return courier details.
 */
public class ReturnPickupShippingPayload extends MinimalPayload {

    private String returnCarrier;
    private String returnTrackingNumber;

    public String getReturnCarrier() { return returnCarrier; }
    public void setReturnCarrier(String returnCarrier) { this.returnCarrier = returnCarrier; }

    public String getReturnTrackingNumber() { return returnTrackingNumber; }
    public void setReturnTrackingNumber(String returnTrackingNumber) { this.returnTrackingNumber = returnTrackingNumber; }
}
