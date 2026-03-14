package com.homebase.ecom.returnrequest.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Central component enforcing all return request policies and rules.
 *
 * <h3>Policies (enforce constraints)</h3>
 * <ul>
 * <li>Return window expiry check</li>
 * <li>Return reason validity</li>
 * <li>Hub inspection required flag</li>
 * <li>Approval threshold for auto-approve vs manual review</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Auto refund on hub receipt</li>
 * <li>Refund processing days</li>
 * <li>Audit comment requirements</li>
 * </ul>
 *
 * All values sourced from {@code returnrequest.json} via {@code CconfigClient}.
 */
@Component
public class ReturnRequestPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getReturnConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("returnrequest", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load returnrequest.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Return window
    // ===========================================================

    /**
     * Returns the max allowed return window in days.
     * Controlled by: returnrequest.json → policies.return.maxReturnWindowDays
     */
    public int getMaxReturnWindowDays() {
        JsonNode node = getReturnConfig().at("/policies/return/maxReturnWindowDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 10;
    }

    /**
     * Validates that the return reason is in the platform's allowed list.
     * Controlled by: returnrequest.json → policies.return.allowedReturnReasons
     */
    public void validateReturnReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            JsonNode required = getReturnConfig().at("/policies/return/requireReturnReason");
            if (required.isMissingNode() || required.asBoolean(true)) {
                throw new IllegalArgumentException("Return reason is required.");
            }
            return;
        }
        List<String> allowed = new ArrayList<>();
        JsonNode node = getReturnConfig().at("/policies/return/allowedReturnReasons");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(r -> allowed.add(r.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(reason.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Return reason '" + reason + "' is not valid. Allowed: " + allowed);
        }
    }

    /**
     * Returns true if hub manager inspection is required before approving a return.
     * Controlled by: returnrequest.json → policies.return.hubInspectionRequired
     */
    public boolean isHubInspectionRequired() {
        JsonNode node = getReturnConfig().at("/policies/return/hubInspectionRequired");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /**
     * Returns true if the return amount qualifies for auto-approval without hub
     * manager review.
     * Controlled by: returnrequest.json → policies.approval.autoApproveBelow
     */
    public boolean canAutoApprove(BigDecimal amount) {
        JsonNode node = getReturnConfig().at("/policies/approval/autoApproveBelow");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 500.0;
        return amount != null && amount.doubleValue() <= threshold;
    }

    /**
     * Returns true if Hub Manager approval is required for high-value returns.
     * Controlled by: returnrequest.json →
     * policies.approval.requireHubManagerApprovalAbove
     */
    public boolean requiresHubManagerApproval(BigDecimal amount) {
        JsonNode node = getReturnConfig().at("/policies/approval/requireHubManagerApprovalAbove");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 5000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    // ===========================================================
    // RULE: Refund
    // ===========================================================

    /**
     * Returns true if refund should be auto-initiated when hub receives the
     * returned item.
     */
    public boolean isAutoRefundOnHubReceipt() {
        JsonNode node = getReturnConfig().at("/rules/refund/autoRefundOnHubReceipt");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /** Returns refund processing time in days. */
    public int getRefundProcessingDays() {
        JsonNode node = getReturnConfig().at("/rules/refund/refundProcessingDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 3;
    }

    // ===========================================================
    // RULE: Audit
    // ===========================================================

    /** Returns true if a comment is required when rejecting a return. */
    public boolean isCommentRequiredOnReject() {
        JsonNode node = getReturnConfig().at("/rules/audit/requireCommentOnReject");
        return node.isMissingNode() || node.asBoolean(true);
    }
}
