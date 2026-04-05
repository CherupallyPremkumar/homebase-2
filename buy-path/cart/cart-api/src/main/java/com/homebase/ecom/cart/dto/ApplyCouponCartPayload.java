package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the applyCoupon event.
 */
public class ApplyCouponCartPayload extends MinimalPayload {
    public String couponCode;
}
