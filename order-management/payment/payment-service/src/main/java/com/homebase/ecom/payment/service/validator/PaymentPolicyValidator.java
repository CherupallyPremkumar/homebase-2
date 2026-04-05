package com.homebase.ecom.payment.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Central component enforcing all payment lifecycle policies and rules.
 *
 * <h3>Two-layer validation:</h3>
 * <ul>
 *   <li><b>Layer 1 -- Cconfig thresholds:</b> reads configurable rules from
 *       {@code org/chenile/config/payment.json} (overridable per tenant in DB).
 *       Used for amount limits, method validation, retry limits.</li>
 *   <li><b>Layer 2 -- Business rules:</b> enforced directly in STM transition actions
 *       via calls to this validator.</li>
 * </ul>
 *
 * Policies sourced from {@code payment.json} via {@code CconfigClient}:
 * - paymentTimeoutMinutes(30), maxRetryAttempts(3), retryDelayMinutes(5)
 * - minPaymentAmount(1), maxPaymentAmount(1000000)
 * - supportedMethods(CARD, UPI, NET_BANKING, WALLET, COD, EMI, BNPL), autoSettleDays(7)
 *
 * Wired as @Bean in PaymentConfiguration — no @Component annotation.
 */
public class PaymentPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(PaymentPolicyValidator.class);

    private final CconfigClient cconfigClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public PaymentPolicyValidator(CconfigClient cconfigClient) {
        this.cconfigClient = cconfigClient;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Amount limits
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates the payment amount is within configured bounds.
     * minPaymentAmount(1) <= amount <= maxPaymentAmount(1000000)
     */
    public void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Payment amount is required");
        }
        JsonNode config = getPaymentConfig();
        int min = configInt(config, "/policies/transaction/minPaymentAmount", 1);
        int max = configInt(config, "/policies/transaction/maxPaymentAmount", 1000000);
        if (amount.compareTo(BigDecimal.valueOf(min)) < 0) {
            throw new IllegalArgumentException("Payment amount " + amount + " is below minimum " + min);
        }
        if (amount.compareTo(BigDecimal.valueOf(max)) > 0) {
            throw new IllegalArgumentException("Payment amount " + amount + " exceeds maximum " + max);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Payment method validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates the payment method is in the supported list.
     * Default: CARD, UPI, NET_BANKING, WALLET
     */
    public void validatePaymentMethod(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        JsonNode config = getPaymentConfig();
        List<String> supported = new ArrayList<>();
        JsonNode node = config.at("/policies/transaction/supportedMethods");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(m -> supported.add(m.asText().toUpperCase()));
        }
        if (supported.isEmpty()) {
            // defaults
            supported.addAll(List.of("CARD", "UPI", "NET_BANKING", "WALLET", "COD", "EMI", "BNPL"));
        }
        if (!supported.contains(method.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Payment method '" + method + "' is not supported. Use: " + supported);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Retry limits
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that retry count has not exceeded the maximum.
     */
    public void validateRetryAllowed(Payment payment) {
        int maxRetries = getMaxRetryAttempts();
        if (payment.getRetryCount() >= maxRetries) {
            throw new IllegalStateException(
                    "Payment retry limit exceeded. Current: " + payment.getRetryCount() + ", max: " + maxRetries);
        }
    }

    public int getMaxRetryAttempts() {
        JsonNode config = getPaymentConfig();
        return configInt(config, "/policies/retry/maxRetryAttempts", 3);
    }

    public int getRetryDelayMinutes() {
        JsonNode config = getPaymentConfig();
        return configInt(config, "/policies/retry/retryDelayMinutes", 5);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Gateway validation
    // ═══════════════════════════════════════════════════════════════════════

    public void validateGateway(String gateway) {
        if (gateway == null || gateway.isBlank()) return;
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

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Timeout and Settlement
    // ═══════════════════════════════════════════════════════════════════════

    public int getPaymentTimeoutMinutes() {
        JsonNode config = getPaymentConfig();
        return configInt(config, "/policies/timeout/paymentTimeoutMinutes", 30);
    }

    public int getAutoSettleDays() {
        JsonNode config = getPaymentConfig();
        return configInt(config, "/policies/settlement/autoSettleDays", 7);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Fraud
    // ═══════════════════════════════════════════════════════════════════════

    public boolean isFlaggedHighValue(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/rules/fraud/flagHighValueTransactionAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 50000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    public boolean requiresVerification(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/rules/fraud/requireVerificationAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 100000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Refund
    // ═══════════════════════════════════════════════════════════════════════

    public int getRefundProcessingDays() {
        JsonNode node = getPaymentConfig().at("/rules/refund/processingDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    public boolean isPartialRefundAllowed() {
        JsonNode node = getPaymentConfig().at("/rules/refund/partialRefundAllowed");
        return node.isMissingNode() || node.asBoolean(true);
    }

    public boolean requiresApproval(BigDecimal amount) {
        JsonNode node = getPaymentConfig().at("/policies/refund/requireRefundApprovalAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 5000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getPaymentConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("payment", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load payment.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }
}
