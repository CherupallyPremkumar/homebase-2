package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product is being published but has no image.
 * Controlled by: product.json → policies.content.requireImageBeforePublish
 */
public class ImageRequiredForPublishException extends PolicyViolationException {
    public ImageRequiredForPublishException() {
        super("product", "Product must have at least one image before it can be published.");
    }

    public ImageRequiredForPublishException(String productId) {
        super("product", "Product '" + productId + "' must have at least one image before it can be published.");
    }
}
