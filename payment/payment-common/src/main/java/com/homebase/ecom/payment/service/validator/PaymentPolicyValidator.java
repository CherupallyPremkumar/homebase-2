package com.homebase.ecom.payment.service.validator;

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
 * Central component enforcing all payment lifecycle policies and rules.
 *
 * <h3>Policies (throw exceptions / enforce constraints)</h3>
 * <ul>
 * <li>Gateway whitelist validation</li>
 * <li>Refund window validation</li>
 * <li>High-value transaction flagging</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Retry: max retries, payment retry window minutes</li>
 * <li>Refund: processing days, partial refund allowed</li>
 * <li>Fraud: thresholds for flagging and blocking</li>
 * </ul>
 *
 * All values sourced from {@code payment.json} via {@code CconfigClient}.
 */
@Component
public class PaymentPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(PaymentPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getPaymentConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("payment", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load payment.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Gateway validation
    // ===========================================================

    /**
     * Validates that the payment gateway is in the platform's allowed list.
     * Controlled by: payment.json → policies.transaction.allowedGateways
     */
    public void validateGateway(String gateway) {
        if (gateway == null || gateway.isBlank())
            return;
        JsonNode config = getPaymentConfig();
        List<String> allowed = new ArrayList<>();
        JsonNode node = config.at("/policies/transaction/allowedGateways");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(g -> allowed.add(g.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(gateway.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Payment gateway '" + gateway + "' is not allowed. Use: " + allowed);
        }
    }

    /**
     * Returns true if a high-value transaction should be flagged for review.
     * Controlled by: payment.json → rules.fraud.flagHighValueTransactionAboveInr
     */
    public boolean isFlaggedHighValue(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/rules/fraud/flagHighValueTransactionAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 50000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    /**
     * Returns true if a transaction requires additional verification.
     * Controlled by: payment.json → rules.fraud.requireVerificationAboveInr
     */
    public boolean requiresVerification(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/rules/fraud/requireVerificationAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 100000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    // ===========================================================
    // RULE: Retry
    // ===========================================================

    /** Returns max payment retry window in minutes. */
    public int getPaymentRetryWindowMinutes() {
        JsonNode node = getPaymentConfig().at("/rules/retry/paymentRetryWindowMinutes");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    /** Returns max allowed payment retry count. */
    public int getMaxPaymentRetries() {
        JsonNode node = getPaymentConfig().at("/rules/retry/maxPaymentRetries");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 3;
    }

    // ===========================================================
    // RULE: Refund
    // ===========================================================

    /** Returns refund processing days. */
    public int getRefundProcessingDays() {
        JsonNode node = getPaymentConfig().at("/rules/refund/processingDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    /** Returns true if partial refunds are allowed. */
    public boolean isPartialRefundAllowed() {
        JsonNode node = getPaymentConfig().at("/rules/refund/partialRefundAllowed");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /** Returns true if refund requires approval above a threshold. */
    public boolean requiresApproval(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/policies/transaction/requireRefundApprovalAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 5000.0;
        return amount != null && amount.doubleValue() > threshold;
    }
}
