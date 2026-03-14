package com.homebase.ecom.inventory.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when an inventory rejection is missing a required comment.
 * Controlled by: inventory.json → policies.stock.requireRejectionComment
 */
public class RejectionCommentRequiredException extends PolicyViolationException {
    public RejectionCommentRequiredException() {
        super("inventory", "A rejection comment is required when rejecting inventory stock.");
    }
}
