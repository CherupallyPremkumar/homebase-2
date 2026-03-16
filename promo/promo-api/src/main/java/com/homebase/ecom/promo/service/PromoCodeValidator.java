package com.homebase.ecom.promo.service;

/**
 * Interface for validating promo codes at the API level.
 * Item 11: Removed Cart dependency -- promo module should not depend on Cart BC.
 * Callers pass cart total and product/category IDs instead.
 */
public interface PromoCodeValidator {

    /**
     * Calculates the discount for a given order value and promo code.
     */
    double calculateDiscount(double orderValue, String promoCode);

    /**
     * Validates whether a promo code is valid for the given order value.
     */
    boolean isValid(double orderValue, String promoCode);
}
