package com.homebase.ecom.order.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a return reason is invalid or not in the allowed list.
 * Controlled by: order.json → policies.return.allowedReturnReasons
 */
public class InvalidReturnReasonException extends PolicyViolationException {
    private final String reason;

    public InvalidReturnReasonException(String reason) {
        super("order", "Return reason '" + reason
                + "' is not valid. Allowed reasons: DAMAGED, WRONG_ITEM, NOT_AS_DESCRIBED, CHANGED_MIND.");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
