package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the courierPickup event.
 */
public class CourierPickupOrderPayload extends MinimalPayload {
    private String carrier;
    private String trackingNumber;
    private Integer estimatedDeliveryDays;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Integer getEstimatedDeliveryDays() {
        return estimatedDeliveryDays;
    }

    public void setEstimatedDeliveryDays(Integer estimatedDeliveryDays) {
        this.estimatedDeliveryDays = estimatedDeliveryDays;
    }
}
