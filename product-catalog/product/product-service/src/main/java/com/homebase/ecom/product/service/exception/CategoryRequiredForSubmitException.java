package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product is submitted for review without a category set.
 * Controlled by: product.json → policies.lifecycle.requireCategoryBeforeSubmit
 */
public class CategoryRequiredForSubmitException extends PolicyViolationException {
    public CategoryRequiredForSubmitException() {
        super("product", "Product must have a category assigned before it can be submitted for review.");
    }
}
