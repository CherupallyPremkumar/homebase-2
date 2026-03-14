package com.homebase.ecom.cart.exception;

public class CartLimitExceededException extends RuntimeException {
    public CartLimitExceededException(String message) {
        super(message);
    }
}
