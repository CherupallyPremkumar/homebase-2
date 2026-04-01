package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for expireCoupon event (SYSTEM-triggered when a coupon expires in Promo service).
 */
public class ExpireCouponCartPayload extends MinimalPayload {
    public String couponCode;
}
