package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Payload for partialShip event -- some items shipped, others still processing.
 */
public class PartialShipOrderPayload extends MinimalPayload {
    private String trackingNumber;
    private String carrier;
    private List<String> shippedItemIds;

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public List<String> getShippedItemIds() { return shippedItemIds; }
    public void setShippedItemIds(List<String> shippedItemIds) { this.shippedItemIds = shippedItemIds; }
}
