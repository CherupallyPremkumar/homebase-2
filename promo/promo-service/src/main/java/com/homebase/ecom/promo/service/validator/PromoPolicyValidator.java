package com.homebase.ecom.promo.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Central component enforcing all promotion and coupon policies and rules.
 *
 * <h3>Policies (enforce constraints)</h3>
 * <ul>
 * <li>Max discount percent / amount</li>
 * <li>Coupon stacking disallowed</li>
 * <li>Minimum order value for coupon usage</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Apply on gross or net amount</li>
 * <li>Rounding strategy</li>
 * </ul>
 *
 * All values sourced from {@code promo.json} via {@code CconfigClient}.
 */
@Component
public class PromoPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(PromoPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getPromoConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("promo", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load promo.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Discount bounds
    // ===========================================================

    /**
     * Validates that a discount percent doesn't exceed the platform maximum.
     * Controlled by: promo.json → policies.discount.maxDiscountPercent
     */
    public void validateDiscountPercent(double discountPercent) {
        JsonNode node = getPromoConfig().at("/policies/discount/maxDiscountPercent");
        double max = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 80.0;
        if (discountPercent > max) {
            throw new IllegalArgumentException(
                    "Discount of " + discountPercent + "% exceeds the platform maximum of " + max + "%.");
        }
    }

    /**
     * Validates calculated discount amount is within the maximum cap.
     * Controlled by: promo.json → policies.discount.maxDiscountAmountInr
     */
    public void validateDiscountAmount(BigDecimal discountAmount) {
        JsonNode node = getPromoConfig().at("/policies/discount/maxDiscountAmountInr");
        double max = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 10000.0;
        if (discountAmount != null && discountAmount.doubleValue() > max) {
            throw new IllegalArgumentException(
                    "Discount amount ₹" + discountAmount + " exceeds the platform cap of ₹" + max + ".");
        }
    }

    /**
     * Returns true if coupon stacking is allowed.
     * Controlled by: promo.json → policies.discount.allowStackingCoupons
     */
    public boolean isCouponStackingAllowed() {
        JsonNode node = getPromoConfig().at("/policies/discount/allowStackingCoupons");
        return !node.isMissingNode() && node.asBoolean(false);
    }

    /**
     * Validates order value meets minimum requirement for coupon use.
     * Controlled by: promo.json → policies.coupon.minOrderValueInr
     */
    public void validateCouponMinOrderValue(BigDecimal orderValue) {
        JsonNode node = getPromoConfig().at("/policies/coupon/minOrderValueInr");
        double min = (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 200.0;
        if (orderValue == null || orderValue.doubleValue() < min) {
            throw new IllegalArgumentException(
                    "Order value ₹" + orderValue + " is below the minimum ₹" + min + " required for this coupon.");
        }
    }

    // ===========================================================
    // RULE: Computation
    // ===========================================================

    /**
     * Returns true if discount should be computed on gross order amount.
     * Controlled by: promo.json → rules.computation.applyOnGrossAmount
     */
    public boolean applyDiscountOnGrossAmount() {
        JsonNode node = getPromoConfig().at("/rules/computation/applyOnGrossAmount");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /**
     * Returns the configured rounding mode for discount computation.
     * Controlled by: promo.json → rules.computation.roundingStrategy
     */
    public RoundingMode getRoundingMode() {
        JsonNode node = getPromoConfig().at("/rules/computation/roundingStrategy");
        String strategy = (!node.isMissingNode()) ? node.asText("HALF_UP") : "HALF_UP";
        try {
            return RoundingMode.valueOf(strategy);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown rounding strategy '{}', defaulting to HALF_UP", strategy);
            return RoundingMode.HALF_UP;
        }
    }

    /** Returns max uses per user for a coupon. */
    public int getMaxCouponUsesPerUser() {
        JsonNode node = getPromoConfig().at("/policies/coupon/maxUsesPerUser");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 1;
    }
}
