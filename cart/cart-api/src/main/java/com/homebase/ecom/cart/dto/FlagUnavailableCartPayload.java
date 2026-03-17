package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for flagUnavailable event (SYSTEM-triggered when stock depleted or product discontinued).
 */
public class FlagUnavailableCartPayload extends MinimalPayload {
    public String variantId;
    public String productId;
}
