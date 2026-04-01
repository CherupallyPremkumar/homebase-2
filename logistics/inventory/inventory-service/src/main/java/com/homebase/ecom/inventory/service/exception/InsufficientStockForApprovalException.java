package com.homebase.ecom.inventory.service.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when approving stock with a quantity below the configured minimum.
 * Controlled by: inventory.json → policies.stock.minApprovalQuantity
 */
public class InsufficientStockForApprovalException extends PolicyViolationException {
    private final int quantity;
    private final int min;

    public InsufficientStockForApprovalException(int quantity, int min) {
        super("inventory", "Stock approval requires at least " + min + " units, but received: " + quantity);
        this.quantity = quantity;
        this.min = min;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMin() {
        return min;
    }
}
