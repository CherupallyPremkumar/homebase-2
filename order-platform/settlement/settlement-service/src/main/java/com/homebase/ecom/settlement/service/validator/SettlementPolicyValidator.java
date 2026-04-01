package com.homebase.ecom.settlement.service.validator;

import com.homebase.ecom.settlement.model.Settlement;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Central component for enforcing all settlement policies and reading rule configurations.
 * Wired as @Bean in SettlementConfiguration (never @Component per Chenile conventions).
 *
 * Cconfig keys (settlement.json):
 * - commissionRatePercent: 15 (default)
 * - platformFeePercent: 2 (default)
 * - settlementCycleDays: 14 (default)
 * - minSettlementAmount: 500 (default, in paise)
 * - maxAdjustmentPercent: 10 (default)
 * - autoApproveThreshold: 10000 (default, in paise)
 */
public class SettlementPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(SettlementPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // CONFIG GETTERS
    // ═══════════════════════════════════════════════════════════════════════

    public BigDecimal getCommissionRatePercent() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/commissionRatePercent");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 15.0;
        return BigDecimal.valueOf(val);
    }

    public BigDecimal getPlatformFeePercent() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/platformFeePercent");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 2.0;
        return BigDecimal.valueOf(val);
    }

    public int getSettlementCycleDays() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/settlementCycleDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 14;
    }

    public long getMinSettlementAmountPaise() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/minSettlementAmount");
        return (!node.isMissingNode() && node.isNumber()) ? node.asLong() : 500L;
    }

    public BigDecimal getMaxAdjustmentPercent() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/maxAdjustmentPercent");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10.0;
        return BigDecimal.valueOf(val);
    }

    public long getAutoApproveThresholdPaise() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/autoApproveThreshold");
        return (!node.isMissingNode() && node.isNumber()) ? node.asLong() : 10000L;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Amount thresholds
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the settlement net amount (in paise) meets the minimum threshold.
     */
    public void validateMinSettlementAmountPaise(long netAmountPaise) {
        long min = getMinSettlementAmountPaise();
        if (netAmountPaise < min) {
            throw new IllegalArgumentException(
                    "Settlement net amount " + netAmountPaise + " paise is below the minimum required " + min
                            + " paise. Settlement will be deferred to next cycle.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Commission validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the commission amount is within expected range for the order amount.
     * Both amounts in paise.
     */
    public void validateCommission(long orderAmountPaise, long commissionAmountPaise) {
        BigDecimal rate = getCommissionRatePercent();
        BigDecimal expectedCommission = BigDecimal.valueOf(orderAmountPaise)
                .multiply(rate)
                .divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP);

        // Allow 1% tolerance
        BigDecimal tolerance = expectedCommission.multiply(new BigDecimal("0.01"));
        BigDecimal diff = BigDecimal.valueOf(commissionAmountPaise).subtract(expectedCommission).abs();
        if (diff.compareTo(tolerance) > 0) {
            log.warn("Commission {} paise deviates from expected {} paise (rate={}%) for order amount {} paise",
                    commissionAmountPaise, expectedCommission, rate, orderAmountPaise);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Adjustment limits
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the adjustment amount does not exceed the max allowed percentage of order amount.
     * Order amount from Money (paise), adjustment amount as BigDecimal (paise).
     */
    public void validateAdjustmentLimit(Settlement settlement, BigDecimal adjustmentAmount) {
        if (settlement.getOrderAmount() == null || adjustmentAmount == null) return;

        BigDecimal maxPercent = getMaxAdjustmentPercent();
        long orderAmountPaise = settlement.getOrderAmount().getAmount();
        BigDecimal maxAllowed = BigDecimal.valueOf(orderAmountPaise)
                .multiply(maxPercent)
                .divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP);

        if (adjustmentAmount.abs().compareTo(maxAllowed) > 0) {
            throw new IllegalArgumentException(
                    "Adjustment amount " + adjustmentAmount.abs() + " exceeds maximum allowed " + maxAllowed
                            + " (" + maxPercent + "% of order amount " + orderAmountPaise + " paise)");
        }
    }

    /**
     * Returns true if the settlement amount (in paise) qualifies for auto-approval.
     */
    public boolean isAutoApproveEligible(long netAmountPaise) {
        long threshold = getAutoApproveThresholdPaise();
        return netAmountPaise <= threshold;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getSettlementConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("settlement", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load settlement.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }
}
