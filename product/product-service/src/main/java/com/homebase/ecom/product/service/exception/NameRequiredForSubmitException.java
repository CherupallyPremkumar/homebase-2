package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product is submitted for review without a name.
 */
public class NameRequiredForSubmitException extends PolicyViolationException {
    public NameRequiredForSubmitException() {
        super("product", "Product name is required before submitting for review.");
    }
}
