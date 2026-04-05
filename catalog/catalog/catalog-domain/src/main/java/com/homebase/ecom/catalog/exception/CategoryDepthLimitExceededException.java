package com.homebase.ecom.catalog.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when creating a category would exceed the configured maximum nesting
 * depth.
 */
public class CategoryDepthLimitExceededException extends PolicyViolationException {
    private final int requestedLevel;
    private final int maxLevel;

    public CategoryDepthLimitExceededException(int requestedLevel, int maxLevel) {
        super("catalog",
                "Category level " + requestedLevel + " exceeds the maximum allowed depth of " + maxLevel + ".");
        this.requestedLevel = requestedLevel;
        this.maxLevel = maxLevel;
    }

    public int getRequestedLevel() {
        return requestedLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
