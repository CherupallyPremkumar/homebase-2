package com.homebase.ecom.cart.dto;

import com.homebase.ecom.shared.Address;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the initiateCheckout event.
 */
public class InitiateCheckoutCartPayload extends MinimalPayload {
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
