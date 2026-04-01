package com.homebase.ecom.inventory.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a reservation quantity exceeds available stock.
 * Controlled by: inventory.json → policies.reservation
 */
public class ReservationQuantityExceededException extends PolicyViolationException {
    private final int requested;
    private final int available;

    public ReservationQuantityExceededException(int requested, int available) {
        super("inventory",
                "Reservation of " + requested + " units exceeds available stock of " + available + " units.");
        this.requested = requested;
        this.available = available;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
