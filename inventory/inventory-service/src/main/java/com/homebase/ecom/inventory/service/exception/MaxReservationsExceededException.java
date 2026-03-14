package com.homebase.ecom.inventory.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when the number of open reservations exceeds the configured maximum.
 * Controlled by: inventory.json → policies.reservation.maxOpenReservations
 */
public class MaxReservationsExceededException extends PolicyViolationException {
    private final int current;
    private final int max;

    public MaxReservationsExceededException(int current, int max) {
        super("inventory", "Maximum open reservations (" + max + ") exceeded. Current: " + current);
        this.current = current;
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }
}
