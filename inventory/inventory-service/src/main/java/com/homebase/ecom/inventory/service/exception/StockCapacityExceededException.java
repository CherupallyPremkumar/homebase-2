package com.homebase.ecom.inventory.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a stock update would exceed the warehouse capacity limit.
 * Controlled by: inventory.json → policies.stock.maxStockCapacity
 */
public class StockCapacityExceededException extends PolicyViolationException {
    private final int current;
    private final int added;
    private final int max;

    public StockCapacityExceededException(int current, int added, int max) {
        super("inventory",
                "Adding " + added + " units to current stock of " + current + " would exceed warehouse capacity of "
                        + max
                        + " units.");
        this.current = current;
        this.added = added;
        this.max = max;
    }

    public StockCapacityExceededException(int requested, int max) {
        this(0, requested, max);
    }

    public int getCurrent() {
        return current;
    }

    public int getAdded() {
        return added;
    }

    public int getMax() {
        return max;
    }
}
