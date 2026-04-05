package com.homebase.ecom.order.model;

/**
 * Order lifecycle states — aligned with STM XML states.
 */
public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    PAYMENT_FAILED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCEL_REQUESTED,
    CANCELLATION_DENIED,
    CANCELLED,
    REFUND_REQUESTED,
    REFUNDED
}
