package com.homebase.ecom.supplier.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Central component enforcing all supplier onboarding and lifecycle policies.
 *
 * <h3>Policies (enforce constraints)</h3>
 * <ul>
 * <li>Bank account required before payout</li>
 * <li>Max products before identity verification</li>
 * <li>Dad approval required for new suppliers</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Rating thresholds for featured products</li>
 * <li>Max active products per supplier</li>
 * <li>Platform commission percent</li>
 * </ul>
 *
 * All values sourced from {@code supplier.json} via {@code CconfigClient}.
 */
@Component
public class SupplierPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(SupplierPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getSupplierConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("supplier", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load supplier.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Bank account required for payout
    // ===========================================================

    /**
     * Validates that a bank account is registered before initiating payout.
     * Controlled by: supplier.json → policies.profile.requireBankAccount
     */
    public void validateBankAccountForPayout(boolean hasBankAccount) {
        JsonNode node = getSupplierConfig().at("/policies/profile/requireBankAccount");
        boolean required = node.isMissingNode() || node.asBoolean(true);
        if (required && !hasBankAccount) {
            throw new IllegalStateException(
                    "Supplier must register a bank account before initiating payout.");
        }
    }

    /**
     * Validates that the supplier hasn't exceeded the product limit before identity
     * verification.
     * Controlled by: supplier.json → policies.profile.maxProductsBeforeVerification
     */
    public void validateProductCountBeforeVerification(int currentProductCount, boolean isVerified) {
        if (isVerified)
            return;
        JsonNode node = getSupplierConfig().at("/policies/profile/maxProductsBeforeVerification");
        int max = (!node.isMissingNode() && node.isInt()) ? node.asInt() : 10;
        if (currentProductCount >= max) {
            throw new IllegalStateException(
                    "Supplier must complete identity verification before adding more products. Limit: " + max);
        }
    }

    /**
     * Returns true if Hub Manager (Dad) approval is required for new supplier
     * activation.
     * Controlled by: supplier.json → policies.onboarding.requireDadApproval
     */
    public boolean isDadApprovalRequired() {
        JsonNode node = getSupplierConfig().at("/policies/onboarding/requireDadApproval");
        return node.isMissingNode() || node.asBoolean(true);
    }

    // ===========================================================
    // RULE: Rating
    // ===========================================================

    /**
     * Returns rating required for a supplier's products to appear in featured
     * sections.
     */
    public double getMinRatingForFeaturedProducts() {
        JsonNode node = getSupplierConfig().at("/rules/rating/minRatingForFeaturedProducts");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 4.0;
    }

    /**
     * Returns rating below which a supplier account should be reviewed for
     * suspension.
     */
    public double getSuspendBelowRating() {
        JsonNode node = getSupplierConfig().at("/rules/rating/suspendBelowRating");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 2.0;
    }

    // ===========================================================
    // RULE: Product limits
    // ===========================================================

    /** Returns maximum allowed active products per supplier. */
    public int getMaxActiveProducts() {
        JsonNode node = getSupplierConfig().at("/rules/product/maxActiveProducts");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 100;
    }

    // ===========================================================
    // RULE: Commission
    // ===========================================================

    /** Returns platform commission percentage. */
    public double getPlatformCommissionPercent() {
        JsonNode node = getSupplierConfig().at("/policies/commission/platformCommissionPercent");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10.0;
    }

    /** Returns number of hold days for new supplier payouts. */
    public int getNewSupplierHoldDays() {
        JsonNode node = getSupplierConfig().at("/policies/onboarding/holdPeriodDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 15;
    }
}
