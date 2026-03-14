package com.homebase.ecom.catalog.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when the number of featured Collections exceeds the configured
 * maximum.
 */
public class MaxFeaturedCollectionsExceededException extends PolicyViolationException {
    private final int current;
    private final int max;

    public MaxFeaturedCollectionsExceededException(int current, int max) {
        super("catalog", "Cannot feature this collection. Already " + current + " featured collections exist (max: "
                + max + ").");
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
