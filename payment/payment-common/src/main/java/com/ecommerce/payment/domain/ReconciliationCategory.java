package com.ecommerce.payment.domain;

import java.util.Locale;

/**
 * Standardized categories for reconciliation mismatches.
 *
 * Keep these gateway-agnostic.
 */
public enum ReconciliationCategory {
    MISSING_IN_INTERNAL,
    MISSING_IN_PROVIDER,
    AMOUNT_MISMATCH,
    STATUS_MISMATCH,
    CURRENCY_MISMATCH,
    FEE_MISMATCH,
    PAYOUT_MISMATCH,
    DUPLICATE,
    UNKNOWN;

    /**
     * Compatibility helper for the legacy reconciliation_mismatches table (V11).
     */
    public static ReconciliationCategory fromLegacyMismatchType(String mismatchType) {
        if (mismatchType == null || mismatchType.isBlank()) {
            return UNKNOWN;
        }

        String normalized = mismatchType.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "MISSING_IN_DB" -> MISSING_IN_INTERNAL;
            case "MISSING_IN_STRIPE" -> MISSING_IN_PROVIDER;
            case "AMOUNT_MISMATCH" -> AMOUNT_MISMATCH;
            case "STATUS_MISMATCH" -> STATUS_MISMATCH;
            case "AMOUNT_AND_STATUS_MISMATCH" -> AMOUNT_MISMATCH; // keep category stable; add details in notes
            default -> UNKNOWN;
        };
    }
}
