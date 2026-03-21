package com.homebase.ecom.payment.domain.model;

/**
 * High-level payment method categories.
 * Maps to the paymentMethod field on Payment.
 */
public enum PaymentMethodCategory {
    CARD,
    UPI,
    NET_BANKING,
    WALLET,
    COD,
    EMI,
    BNPL
}
