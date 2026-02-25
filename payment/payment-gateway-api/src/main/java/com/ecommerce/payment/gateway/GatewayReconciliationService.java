package com.ecommerce.payment.gateway;

import com.ecommerce.payment.gateway.dto.SettlementReportRow;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for reconciling internal transaction records with gateway reports.
 */
public interface GatewayReconciliationService {
    /**
     * Supports a specific gateway type?
     */
    boolean supports(String gatewayType);

    /**
     * Downloads and parses the settlement report for a specific date.
     *
     * @return Normalized settlement rows from the gateway
     */
    List<SettlementReportRow> fetchSettlementReport(LocalDate date);
}
