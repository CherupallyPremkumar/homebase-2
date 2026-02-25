package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the updateQuantity event.
 */
public class UpdateQuantityCartPayload extends MinimalPayload {
    public String productId;
    public Integer quantity;
}
