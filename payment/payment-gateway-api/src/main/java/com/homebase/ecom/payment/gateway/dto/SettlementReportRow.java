package com.homebase.ecom.payment.gateway.dto;

import java.time.Instant;

/**
 * A gateway-agnostic, normalized row from a provider settlement report.
 *
 * Amounts are expressed in the provider's smallest currency unit (e.g., paise for INR).
 */
public record SettlementReportRow(
        String providerTransactionId,
        String providerObjectType,
        long amountMinor,
        long feeMinor,
        long netMinor,
        String currency,
        String status,
        String payoutId,
        Instant createdAt
) {
}
