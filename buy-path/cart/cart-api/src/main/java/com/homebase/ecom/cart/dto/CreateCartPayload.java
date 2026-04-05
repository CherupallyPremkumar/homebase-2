package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for creating a new cart.
 */
public class CreateCartPayload extends MinimalPayload {
    public String customerId;
    public String sessionId;
}
