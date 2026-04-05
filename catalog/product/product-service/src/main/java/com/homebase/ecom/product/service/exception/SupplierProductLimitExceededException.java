package com.homebase.ecom.product.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a supplier has exceeded the configured maximum number of active
 * products.
 * Controlled by: product.json → policies.supplier.maxProductsPerSupplier
 */
public class SupplierProductLimitExceededException extends PolicyViolationException {
    private final int current;
    private final int max;

    public SupplierProductLimitExceededException(int current, int max) {
        super("product", "Supplier has reached the product limit (" + current + "/" + max
                + "). Contact support to increase your limit.");
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
