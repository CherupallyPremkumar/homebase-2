package com.ecommerce.payment.domain;

/**
 * Workflow status for a reconciliation item.
 */
public enum ReconciliationItemStatus {
    OPEN,
    IN_REVIEW,
    RESOLVED,
    IGNORED;

    public boolean isClosed() {
        return this == RESOLVED || this == IGNORED;
    }
}
