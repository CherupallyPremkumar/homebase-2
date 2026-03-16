package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the completeCheckout event (SYSTEM-triggered after order creation).
 */
public class CompleteCheckoutCartPayload extends MinimalPayload {
    public String orderId;
}
