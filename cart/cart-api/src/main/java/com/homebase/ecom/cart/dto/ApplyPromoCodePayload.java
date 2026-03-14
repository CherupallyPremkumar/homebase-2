package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for applying a promo code to the cart.
 */
public class ApplyPromoCodePayload extends MinimalPayload {
    private String promoCode;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
