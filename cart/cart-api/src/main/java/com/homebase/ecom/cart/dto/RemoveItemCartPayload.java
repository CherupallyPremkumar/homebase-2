package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the removeItem event.
 */
public class RemoveItemCartPayload extends MinimalPayload {
    public String productId;
}
