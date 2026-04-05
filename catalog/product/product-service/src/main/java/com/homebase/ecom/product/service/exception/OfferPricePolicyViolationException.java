package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when an offer price violates platform pricing policies.
 * Controlled by: product.json → policies.pricing
 */
public class OfferPricePolicyViolationException extends PolicyViolationException {
    public OfferPricePolicyViolationException(String message) {
        super("product", message);
    }

    public OfferPricePolicyViolationException(java.math.BigDecimal amount, java.math.BigDecimal min,
            java.math.BigDecimal max) {
        super("product", "Offer price ₹" + amount + " is outside the allowed range [₹" + min + " – ₹" + max + "].");
    }
}
