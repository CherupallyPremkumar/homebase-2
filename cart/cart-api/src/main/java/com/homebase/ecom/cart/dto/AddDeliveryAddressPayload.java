package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;
import com.homebase.ecom.shared.Address;

/**
 * Payload for the addDeliveryAddress event.
 */
public class AddDeliveryAddressPayload extends MinimalPayload {
    private Address shippingAddress;
    private Address billingAddress;

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
