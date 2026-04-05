package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the pickUp event: LABEL_CREATED -> PICKED_UP.
 * Carrier confirms package pickup from warehouse.
 */
public class PickUpShippingPayload extends MinimalPayload {

    private String pickupLocation;

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
}
