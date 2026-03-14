package com.homebase.ecom.checkout.domain.exception;

public class CheckoutException extends RuntimeException {
    public CheckoutException(String message) { super(message); }
    public CheckoutException(String message, Throwable cause) { super(message, cause); }
}
