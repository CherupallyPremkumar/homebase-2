package com.homebase.ecom.cart.exception;

import org.chenile.base.exception.ErrorNumException;

public class QuantityLimitExceededException extends ErrorNumException {
    public QuantityLimitExceededException(String message) {
        super(400, 4002, message);
    }
}
