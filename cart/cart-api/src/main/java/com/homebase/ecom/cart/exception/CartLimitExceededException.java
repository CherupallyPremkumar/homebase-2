package com.homebase.ecom.cart.exception;

import org.chenile.base.exception.ErrorNumException;

public class CartLimitExceededException extends ErrorNumException {
    public CartLimitExceededException(String message) {
        super(400, 4001, message);
    }
}
