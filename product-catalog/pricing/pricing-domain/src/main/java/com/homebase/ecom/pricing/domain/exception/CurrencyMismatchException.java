package com.homebase.ecom.pricing.domain.exception;

public class CurrencyMismatchException extends PricingException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}
