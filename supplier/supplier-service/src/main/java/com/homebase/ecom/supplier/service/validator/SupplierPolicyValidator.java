package com.homebase.ecom.supplier.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.supplier.model.Supplier;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Central policy validator for supplier lifecycle.
 * All thresholds sourced from supplier.json via CconfigClient.
 *
 * Cconfig keys:
 *   minRatingForApproval (3.0)
 *   maxProductsPerSupplier (500)
 *   commissionRateDefault (15)
 *   reviewPeriodDays (30)
 *   performanceReviewIntervalDays (90)
 *   autoSuspendBelowRating (2.0)
 *   minFulfillmentRate (85)
 */
public class SupplierPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(SupplierPolicyValidator.class);

    @Autowired(required = false)
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
    // POLICY: Approval validation
    // ===========================================================

    /**
     * Validates supplier meets minimum criteria for approval.
     * Checks rating threshold from cconfig (minRatingForApproval).
     */
    public void validateForApproval(Supplier supplier) {
        double minRating = getMinRatingForApproval();
        if (supplier.getRating() > 0 && supplier.getRating() < minRating) {
            throw new IllegalStateException(
                    "Supplier rating " + supplier.getRating() + " is below minimum " + minRating + " for approval.");
        }
    }

    /**
     * Validates that supplier hasn't exceeded product limit.
     */
    public void validateProductLimit(int currentProductCount) {
        int max = getMaxProductsPerSupplier();
        if (currentProductCount >= max) {
            throw new IllegalStateException(
                    "Supplier has reached maximum product limit of " + max);
        }
    }

    /**
     * Checks if supplier performance warrants probation.
     * Returns true if rating < autoSuspendBelowRating OR fulfillmentRate < minFulfillmentRate.
     */
    public boolean isPerformanceBelowThreshold(Supplier supplier) {
        double minRating = getAutoSuspendBelowRating();
        double minFulfillment = getMinFulfillmentRate();

        return supplier.getRating() < minRating || supplier.getFulfillmentRate() < minFulfillment;
    }

    /**
     * Validates fulfillment rate meets minimum threshold.
     */
    public void validateFulfillmentRate(double fulfillmentRate) {
        double min = getMinFulfillmentRate();
        if (fulfillmentRate < min) {
            throw new IllegalStateException(
                    "Fulfillment rate " + fulfillmentRate + "% is below minimum " + min + "%.");
        }
    }

    // ===========================================================
    // RULE: Config value getters
    // ===========================================================

    public double getMinRatingForApproval() {
        JsonNode node = getSupplierConfig().at("/minRatingForApproval");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 3.0;
    }

    public int getMaxProductsPerSupplier() {
        JsonNode node = getSupplierConfig().at("/maxProductsPerSupplier");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 500;
    }

    public double getCommissionRateDefault() {
        JsonNode node = getSupplierConfig().at("/commissionRateDefault");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 15.0;
    }

    public int getReviewPeriodDays() {
        JsonNode node = getSupplierConfig().at("/reviewPeriodDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    public int getPerformanceReviewIntervalDays() {
        JsonNode node = getSupplierConfig().at("/performanceReviewIntervalDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 90;
    }

    public double getAutoSuspendBelowRating() {
        JsonNode node = getSupplierConfig().at("/autoSuspendBelowRating");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 2.0;
    }

    public double getMinFulfillmentRate() {
        JsonNode node = getSupplierConfig().at("/minFulfillmentRate");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 85.0;
    }
}
