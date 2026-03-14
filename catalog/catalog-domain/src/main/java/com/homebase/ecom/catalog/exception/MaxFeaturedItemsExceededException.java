package com.homebase.ecom.catalog.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when the number of featured CatalogItems exceeds the configured
 * maximum.
 */
public class MaxFeaturedItemsExceededException extends PolicyViolationException {
    private final int current;
    private final int max;

    public MaxFeaturedItemsExceededException(int current, int max) {
        super("catalog", "Cannot feature this item. Already " + current + " featured items exist (max: " + max + ").");
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
