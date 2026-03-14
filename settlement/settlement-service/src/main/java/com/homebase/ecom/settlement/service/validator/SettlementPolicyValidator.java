package com.homebase.ecom.settlement.service.validator;

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
 * Central component enforcing all settlement payout policies and rules.
 *
 * <h3>Policies (enforce constraints)</h3>
 * <ul>
 * <li>Minimum payout balance</li>
 * <li>Allowed payout methods</li>
 * <li>New supplier hold period before first payout</li>
 * <li>Approval required above threshold</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Reconciliation period, tolerance amount</li>
 * <li>Ledger: auto-offset on return, retention days</li>
 * </ul>
 *
 * All values sourced from {@code settlement.json} via {@code CconfigClient}.
 */
@Component
public class SettlementPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(SettlementPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getSettlementConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("settlement", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load settlement.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Minimum payout balance
    // ===========================================================

    /**
     * Validates that a supplier's balance meets the minimum payout threshold.
     * Controlled by: settlement.json → policies.payout.minPayoutBalanceInr
     */
    public void validateMinPayoutBalance(BigDecimal balance) {
        JsonNode node = getSettlementConfig().at("/policies/payout/minPayoutBalanceInr");
        double min = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 1000.0;
        if (balance == null || balance.doubleValue() < min) {
            throw new IllegalArgumentException(
                    "Payout balance ₹" + balance + " is below the minimum required ₹" + min
                            + ". Payout will be deferred to next cycle.");
        }
    }

    /**
     * Validates that the payout method is allowed.
     * Controlled by: settlement.json → policies.payout.allowedPayoutMethods
     */
    public void validatePayoutMethod(String method) {
        if (method == null || method.isBlank())
            return;
        JsonNode config = getSettlementConfig();
        List<String> allowed = new ArrayList<>();
        JsonNode node = config.at("/policies/payout/allowedPayoutMethods");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(m -> allowed.add(m.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(method.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Payout method '" + method + "' is not allowed. Use: " + allowed);
        }
    }

    /**
     * Returns true if a payout amount requires manual approval.
     * Controlled by: settlement.json → policies.payout.requireApprovalAboveInr
     */
    public boolean requiresApproval(BigDecimal amount) {
        JsonNode node = getSettlementConfig().at("/policies/payout/requireApprovalAboveInr");
        double threshold = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 50000.0;
        return amount != null && amount.doubleValue() > threshold;
    }

    // ===========================================================
    // RULE: Reconciliation
    // ===========================================================

    /** Returns reconciliation period in days. */
    public int getReconcilePeriodDays() {
        JsonNode node = getSettlementConfig().at("/rules/reconciliation/reconcilePeriodDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    /** Returns tolerance amount in INR for reconciliation. */
    public double getReconciliationToleranceInr() {
        JsonNode node = getSettlementConfig().at("/rules/reconciliation/toleranceAmountInr");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10.0;
    }

    // ===========================================================
    // RULE: Ledger
    // ===========================================================

    /** Returns true if returns should auto-offset against the supplier ledger. */
    public boolean isAutoOffsetOnReturn() {
        JsonNode node = getSettlementConfig().at("/rules/ledger/autoOffsetOnReturn");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /** Returns how many days ledger entries should be retained. */
    public int getLedgerRetentionDays() {
        JsonNode node = getSettlementConfig().at("/rules/ledger/retainLedgerEntryDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 1825;
    }

    /** Returns the payout cycle in days. */
    public int getPayoutCycleDays() {
        JsonNode node = getSettlementConfig().at("/policies/payout/payoutCycleDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }
}
