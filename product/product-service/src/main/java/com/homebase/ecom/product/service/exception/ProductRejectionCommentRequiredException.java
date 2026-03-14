package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a product rejection is missing a required comment.
 * Controlled by: product.json → policies.review.requireRejectionComment
 */
public class ProductRejectionCommentRequiredException extends PolicyViolationException {
    public ProductRejectionCommentRequiredException() {
        super("product", "A comment is required when rejecting a product. Please provide feedback to the supplier.");
    }
}
