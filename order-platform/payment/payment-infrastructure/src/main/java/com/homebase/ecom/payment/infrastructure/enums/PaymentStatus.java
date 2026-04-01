package com.homebase.ecom.payment.infrastructure.enums;

/**
 * Status of a payment transaction as stored in {@link PaymentTransaction#getStatus()}.
 *
 * This enum is intentionally mapped to the existing String values
 * used in the database (e.g. "SUCCEEDED", "FAILED", "REFUNDED").
 */
public enum PaymentStatus {
    SUCCEEDED,
    FAILED,
    REFUNDED
}

