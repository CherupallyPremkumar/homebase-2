package com.homebase.ecom.disputes.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Central component enforcing all dispute policies and rules.
 *
 * <h3>Policies (cconfig: disputes.json)</h3>
 * <ul>
 * <li>disputeWindowDays (60) - max days after order to raise dispute</li>
 * <li>maxDisputesPerOrder (3) - max disputes per order</li>
 * <li>slaTargetDays (5) - target SLA days for dispute resolution</li>
 * <li>autoEscalateAfterDays (3) - auto-escalate if unresolved after N days</li>
 * <li>maxRefundPercent (100) - max refund as percent of disputed amount</li>
 * </ul>
 */
public class DisputePolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(DisputePolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getDisputeConfig() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("disputes", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load disputes config from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Dispute window (default 60 days)
    // ===========================================================

    public int getDisputeWindowDays() {
        JsonNode node = getDisputeConfig().at("/policies/dispute/disputeWindowDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 60;
    }

    public void validateDisputeWindow(LocalDateTime orderDate) {
        if (orderDate != null) {
            long daysSinceOrder = ChronoUnit.DAYS.between(orderDate, LocalDateTime.now());
            int maxWindow = getDisputeWindowDays();
            if (daysSinceOrder > maxWindow) {
                throw new IllegalArgumentException("Dispute window expired. Maximum " + maxWindow
                        + " days allowed, but " + daysSinceOrder + " days have passed since the order.");
            }
        }
    }

    // ===========================================================
    // POLICY: Max disputes per order (default 3)
    // ===========================================================

    public int getMaxDisputesPerOrder() {
        JsonNode node = getDisputeConfig().at("/policies/dispute/maxDisputesPerOrder");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 3;
    }

    public void validateDisputeCount(int disputeCount) {
        int max = getMaxDisputesPerOrder();
        if (disputeCount >= max) {
            throw new IllegalArgumentException("Maximum " + max + " disputes per order, but " + disputeCount + " already exist.");
        }
    }

    // ===========================================================
    // POLICY: SLA target days (default 5)
    // ===========================================================

    public int getSlaTargetDays() {
        JsonNode node = getDisputeConfig().at("/policies/sla/slaTargetDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    // ===========================================================
    // POLICY: Auto-escalate after days (default 3)
    // ===========================================================

    public int getAutoEscalateAfterDays() {
        JsonNode node = getDisputeConfig().at("/policies/sla/autoEscalateAfterDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 3;
    }

    // ===========================================================
    // POLICY: Max refund percent (default 100)
    // ===========================================================

    public double getMaxRefundPercent() {
        JsonNode node = getDisputeConfig().at("/policies/refund/maxRefundPercent");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 100.0;
    }

    public void validateRefundAmount(BigDecimal refundAmount, BigDecimal disputedAmount) {
        if (refundAmount != null && disputedAmount != null && disputedAmount.compareTo(BigDecimal.ZERO) > 0) {
            double refundPercent = refundAmount.doubleValue() / disputedAmount.doubleValue() * 100.0;
            double maxPercent = getMaxRefundPercent();
            if (refundPercent > maxPercent) {
                throw new IllegalArgumentException(
                        "Refund amount exceeds maximum allowed. Max " + maxPercent + "% of disputed amount.");
            }
        }
    }

    // ===========================================================
    // VALIDATION: Dispute type
    // ===========================================================

    public void validateDisputeType(String disputeType) {
        if (disputeType == null || disputeType.trim().isEmpty()) {
            throw new IllegalArgumentException("Dispute type is required.");
        }
        List<String> allowed = new ArrayList<>();
        JsonNode node = getDisputeConfig().at("/policies/dispute/allowedDisputeTypes");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(r -> allowed.add(r.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(disputeType.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Dispute type '" + disputeType + "' is not valid. Allowed: " + allowed);
        }
    }

    // ===========================================================
    // VALIDATION: Dispute reason
    // ===========================================================

    public void validateDisputeReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            JsonNode required = getDisputeConfig().at("/policies/dispute/requireDisputeReason");
            if (required.isMissingNode() || required.asBoolean(true)) {
                throw new IllegalArgumentException("Dispute reason is required.");
            }
        }
    }
}
