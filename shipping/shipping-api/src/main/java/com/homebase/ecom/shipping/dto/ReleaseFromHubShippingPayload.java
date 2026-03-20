package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the releaseFromHub event: HELD_AT_HUB -> DELIVERED.
 * Customer picks up from hub or carrier releases for delivery.
 */
public class ReleaseFromHubShippingPayload extends MinimalPayload {

    private String pickedUpBy;

    public String getPickedUpBy() { return pickedUpBy; }
    public void setPickedUpBy(String pickedUpBy) { this.pickedUpBy = pickedUpBy; }
}
