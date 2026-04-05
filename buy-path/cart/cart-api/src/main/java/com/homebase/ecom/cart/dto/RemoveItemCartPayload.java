package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the removeItem event.
 */
public class RemoveItemCartPayload extends MinimalPayload {
    public String variantId;
}
