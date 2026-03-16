package com.homebase.ecom.checkout.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the process event (initiate checkout saga).
 * Customer provides cartId, addresses, shipping/payment method.
 */
public class InitiateCheckoutPayload extends MinimalPayload {
    public String cartId;
    public String shippingAddressId;
    public String billingAddressId;
    public String shippingMethod;
    public String paymentMethodId;
}
