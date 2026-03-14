package com.homebase.ecom.order.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a return is attempted outside the return window.
 * Controlled by: order.json → policies.return.returnWindowDays
 */
public class ReturnWindowExpiredException extends PolicyViolationException {
    private final int windowDays;

    public ReturnWindowExpiredException(int windowDays) {
        super("order", "Return window of " + windowDays + " days has expired. Returns must be initiated within "
                + windowDays + " days of delivery.");
        this.windowDays = windowDays;
    }

    public int getWindowDays() {
        return windowDays;
    }
}
