package com.homebase.ecom.shipping.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the updateAddress event: IN_TRANSIT/DELIVERY_FAILED -> same state.
 * Customer or admin updates the delivery address.
 */
public class UpdateAddressShippingPayload extends MinimalPayload {

    private String newAddress;

    public String getNewAddress() { return newAddress; }
    public void setNewAddress(String newAddress) { this.newAddress = newAddress; }
}
