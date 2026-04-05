package com.homebase.ecom.order.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a cancellation is attempted outside the allowed window.
 * Controlled by: order.json → policies.cancellation.cancellationWindowHours
 */
public class CancellationWindowExpiredException extends PolicyViolationException {
    private final int windowHours;

    public CancellationWindowExpiredException(int windowHours) {
        super("order",
                "Order cancellation window of " + windowHours + " hours has expired. Contact support for assistance.");
        this.windowHours = windowHours;
    }

    public int getWindowHours() {
        return windowHours;
    }
}
