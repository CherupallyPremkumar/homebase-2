package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the updateQuantity event.
 */
public class UpdateQuantityCartPayload extends MinimalPayload {
    public String variantId;
    public int quantity;
}
