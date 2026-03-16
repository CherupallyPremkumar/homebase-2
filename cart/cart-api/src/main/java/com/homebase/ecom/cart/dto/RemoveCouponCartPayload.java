package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the removeCoupon event.
 */
public class RemoveCouponCartPayload extends MinimalPayload {
    public String couponCode;
}
