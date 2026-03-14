package com.homebase.ecom.cart.exception;

public class QuantityLimitExceededException extends RuntimeException {
    public QuantityLimitExceededException(String message) {
        super(message);
    }
}
