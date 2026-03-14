package com.homebase.ecom.cart.exception;

/**
 * Exception thrown when a cart operation violates the multi-seller policy
 * (e.g., trying to add items from a second seller when only one is allowed).
 */
public class MultiSellerViolationException extends RuntimeException {
    public MultiSellerViolationException(String message) {
        super(message);
    }
}
