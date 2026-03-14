package com.homebase.ecom.promo.service;

import com.homebase.ecom.cart.model.Cart;

/**
 * Interface for validating promo codes.
 */
public interface PromoCodeValidator {
    double calculateDiscount(Cart cart, String promoCode);
    boolean isValid(Cart cart, String promoCode);
}
