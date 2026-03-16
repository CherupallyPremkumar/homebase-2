package com.homebase.ecom.settlement.service.validator;

import com.homebase.ecom.settlement.model.Settlement;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Central component for enforcing all settlement policies and reading rule configurations.
 *
 * Cconfig keys (settlement.json):
 * - commissionRatePercent: 15 (default)
 * - platformFeePercent: 2 (default)
 * - settlementCycleDays: 14 (default)
 * - minSettlementAmount: 500 (default)
 * - maxAdjustmentPercent: 10 (default)
 * - autoApproveThreshold: 10000 (default)
 */
@Component
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

    public BigDecimal getMinSettlementAmount() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/minSettlementAmount");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 500.0;
        return BigDecimal.valueOf(val);
    }

    public BigDecimal getMaxAdjustmentPercent() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/maxAdjustmentPercent");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10.0;
        return BigDecimal.valueOf(val);
    }

    public BigDecimal getAutoApproveThreshold() {
        JsonNode config = getSettlementConfig();
        JsonNode node = config.at("/policies/autoApproveThreshold");
        double val = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10000.0;
        return BigDecimal.valueOf(val);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Amount thresholds
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the settlement net amount meets the minimum threshold.
     */
    public void validateMinSettlementAmount(BigDecimal netAmount) {
        BigDecimal min = getMinSettlementAmount();
        if (netAmount != null && netAmount.compareTo(min) < 0) {
            throw new IllegalArgumentException(
                    "Settlement net amount " + netAmount + " is below the minimum required " + min
                            + ". Settlement will be deferred to next cycle.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Commission validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the commission amount is within expected range for the order amount.
     */
    public void validateCommission(BigDecimal orderAmount, BigDecimal commissionAmount) {
        if (orderAmount == null || commissionAmount == null) return;

        BigDecimal rate = getCommissionRatePercent();
        BigDecimal expectedCommission = orderAmount.multiply(rate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        // Allow 1% tolerance
        BigDecimal tolerance = expectedCommission.multiply(new BigDecimal("0.01"));
        BigDecimal diff = commissionAmount.subtract(expectedCommission).abs();
        if (diff.compareTo(tolerance) > 0) {
            log.warn("Commission {} deviates from expected {} (rate={}%) for order amount {}",
                    commissionAmount, expectedCommission, rate, orderAmount);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Adjustment limits
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the adjustment amount does not exceed the max allowed percentage of order amount.
     */
    public void validateAdjustmentLimit(Settlement settlement, BigDecimal adjustmentAmount) {
        if (settlement.getOrderAmount() == null || adjustmentAmount == null) return;

        BigDecimal maxPercent = getMaxAdjustmentPercent();
        BigDecimal orderAmount = settlement.getOrderAmount().getAmount();
        BigDecimal maxAllowed = orderAmount.multiply(maxPercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        if (adjustmentAmount.abs().compareTo(maxAllowed) > 0) {
            throw new IllegalArgumentException(
                    "Adjustment amount " + adjustmentAmount.abs() + " exceeds maximum allowed " + maxAllowed
                            + " (" + maxPercent + "% of order amount " + orderAmount + ")");
        }
    }

    /**
     * Returns true if the settlement amount qualifies for auto-approval.
     */
    public boolean isAutoApproveEligible(BigDecimal netAmount) {
        BigDecimal threshold = getAutoApproveThreshold();
        return netAmount != null && netAmount.compareTo(threshold) <= 0;
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
