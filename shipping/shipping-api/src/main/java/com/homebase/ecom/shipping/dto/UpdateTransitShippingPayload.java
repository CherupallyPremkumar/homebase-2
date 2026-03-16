package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the updateTransit event: PICKED_UP/IN_TRANSIT -> IN_TRANSIT.
 * Contains current location and updated ETA information.
 */
public class UpdateTransitShippingPayload extends MinimalPayload {

    private String currentLocation;
    private int updatedEtaDays;

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public int getUpdatedEtaDays() { return updatedEtaDays; }
    public void setUpdatedEtaDays(int updatedEtaDays) { this.updatedEtaDays = updatedEtaDays; }
}
