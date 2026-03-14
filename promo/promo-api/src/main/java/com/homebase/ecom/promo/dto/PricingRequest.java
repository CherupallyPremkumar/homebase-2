package com.homebase.ecom.promo.dto;

import com.homebase.ecom.promo.model.CartSnapshot;

import java.io.Serializable;

public class PricingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private CartSnapshot cart;
    private String couponCode;

    public PricingRequest() {
    }

    public CartSnapshot getCart() {
        return cart;
    }

    public void setCart(CartSnapshot cart) {
        this.cart = cart;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
