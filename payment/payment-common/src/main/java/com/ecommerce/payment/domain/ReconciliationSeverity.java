package com.ecommerce.payment.domain;

/**
 * Severity indicates business impact / urgency of a reconciliation item.
 */
public enum ReconciliationSeverity {
    INFO,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static ReconciliationSeverity defaultFor(ReconciliationCategory category) {
        if (category == null) {
            return MEDIUM;
        }

        return switch (category) {
            case MISSING_IN_INTERNAL, MISSING_IN_PROVIDER -> HIGH;
            case AMOUNT_MISMATCH, FEE_MISMATCH, PAYOUT_MISMATCH, CURRENCY_MISMATCH -> CRITICAL;
            case STATUS_MISMATCH, DUPLICATE -> MEDIUM;
            case UNKNOWN -> LOW;
        };
    }
}
