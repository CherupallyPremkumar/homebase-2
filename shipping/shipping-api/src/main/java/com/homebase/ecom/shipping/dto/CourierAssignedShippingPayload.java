package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the courierAssigned event.
 * Contains carrier information and optional tracking details.
 */
public class CourierAssignedShippingPayload extends MinimalPayload {

    private String carrier;
    private String trackingNumber;
    private int estimatedDeliveryDays = 5;

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public int getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public void setEstimatedDeliveryDays(int estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }
}
