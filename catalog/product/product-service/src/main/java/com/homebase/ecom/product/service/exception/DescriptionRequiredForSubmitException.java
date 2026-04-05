package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product is submitted for review without a description.
 */
public class DescriptionRequiredForSubmitException extends PolicyViolationException {
    public DescriptionRequiredForSubmitException() {
        super("product", "Description is required before submitting for review.");
    }
}
