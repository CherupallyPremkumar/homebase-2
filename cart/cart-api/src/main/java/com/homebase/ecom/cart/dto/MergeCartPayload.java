package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for merging a guest/source cart into the target (user) cart.
 * Frontend sends sourceCartId; backend fetches and merges items.
 */
public class MergeCartPayload extends MinimalPayload {
    public String sourceCartId;
}
