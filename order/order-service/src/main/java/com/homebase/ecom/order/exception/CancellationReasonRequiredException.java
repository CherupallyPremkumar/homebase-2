package com.homebase.ecom.order.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a cancellation reason is missing but required by policy.
 * Controlled by: order.json → policies.cancellation.requireCancellationReason
 */
public class CancellationReasonRequiredException extends PolicyViolationException {
    public CancellationReasonRequiredException() {
        super("order",
                "A cancellation reason is required by platform policy. Please provide a reason before cancelling.");
    }
}
