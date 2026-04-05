package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the createLabel event: PENDING -> LABEL_CREATED.
 * Contains carrier assignment and tracking details.
 */
public class CreateLabelShippingPayload extends MinimalPayload {

    private String carrier;
    private String trackingNumber;
    private String shippingMethod; // STANDARD, EXPRESS, OVERNIGHT
    private int estimatedDeliveryDays = 5;

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }

    public int getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public void setEstimatedDeliveryDays(int estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }
}
