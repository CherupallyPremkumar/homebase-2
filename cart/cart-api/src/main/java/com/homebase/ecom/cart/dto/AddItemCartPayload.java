package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the addItem event.
 */
public class AddItemCartPayload extends MinimalPayload {
    public String productId;
    public Integer quantity;
}
