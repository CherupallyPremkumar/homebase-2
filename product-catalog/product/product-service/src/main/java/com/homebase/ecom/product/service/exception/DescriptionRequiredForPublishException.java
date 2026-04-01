package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product is being published but has no description.
 * Controlled by: product.json →
 * policies.content.requireDescriptionBeforePublish
 */
public class DescriptionRequiredForPublishException extends PolicyViolationException {
    public DescriptionRequiredForPublishException() {
        super("product", "Product must have a description before it can be published.");
    }

    public DescriptionRequiredForPublishException(String productId) {
        super("product",
                "Product '" + productId + "' must have a description of at least the required minimum length.");
    }
}
