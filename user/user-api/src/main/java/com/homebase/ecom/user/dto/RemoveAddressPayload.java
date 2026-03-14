package com.homebase.ecom.user.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the removeAddress event (self-transition on ACTIVE).
 */
public class RemoveAddressPayload extends MinimalPayload {
    private String addressId;

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }
}
