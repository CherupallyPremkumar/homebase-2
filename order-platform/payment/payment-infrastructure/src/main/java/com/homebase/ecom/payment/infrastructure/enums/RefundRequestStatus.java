package com.homebase.ecom.payment.infrastructure.enums;

/**
 * Lifecycle states for {@link RefundRequest}.
 *
 * These values are mapped to the Chenile state machine stateIds
 * and persisted as such.
 */
public enum RefundRequestStatus {
    PENDING,
    APPROVED,
    INITIATED,
    PROCESSED
}

