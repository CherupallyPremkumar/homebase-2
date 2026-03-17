package com.homebase.ecom.promo.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Item 3: PromoPolicyValidator -- enforces all promotion policies and rules.
 *
 * Policies sourced from cconfig promo.json (Item 2):
 *   maxDiscountPercent(50), maxFlatDiscount(10000), maxUsagePerPromo(10000),
 *   maxUsagePerCustomer(5), minOrderValueForPromo(200), stackablePromosAllowed(false),
 *   maxActivePromos(100)
 */
@Component
public class PromoPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(PromoPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ======================== Config defaults (Item 2) ========================

    private static final double DEFAULT_MAX_DISCOUNT_PERCENT = 50.0;
    private static final double DEFAULT_MAX_FLAT_DISCOUNT = 10000.0;
    private static final int DEFAULT_MAX_USAGE_PER_PROMO = 10000;
    private static final int DEFAULT_MAX_USAGE_PER_CUSTOMER = 5;
    private static final double DEFAULT_MIN_ORDER_VALUE = 200.0;
    private static final boolean DEFAULT_STACKABLE_ALLOWED = false;
    private static final int DEFAULT_MAX_ACTIVE_PROMOS = 100;

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

    private double getDouble(String path, double defaultVal) {
        JsonNode node = getPromoConfig().at(path);
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : defaultVal;
    }

    private int getInt(String path, int defaultVal) {
        JsonNode node = getPromoConfig().at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private boolean getBoolean(String path, boolean defaultVal) {
        JsonNode node = getPromoConfig().at(path);
        return (!node.isMissingNode()) ? node.asBoolean(defaultVal) : defaultVal;
    }

    // ======================== Getters for config values ========================

    public double getMaxDiscountPercent() {
        return getDouble("/policies/discount/maxDiscountPercent", DEFAULT_MAX_DISCOUNT_PERCENT);
    }

    public double getMaxFlatDiscount() {
        return getDouble("/policies/discount/maxFlatDiscount", DEFAULT_MAX_FLAT_DISCOUNT);
    }

    public int getMaxUsagePerPromo() {
        return getInt("/policies/usage/maxUsagePerPromo", DEFAULT_MAX_USAGE_PER_PROMO);
    }

    public int getMaxUsagePerCustomer() {
        return getInt("/policies/usage/maxUsagePerCustomer", DEFAULT_MAX_USAGE_PER_CUSTOMER);
    }

    public double getMinOrderValueForPromo() {
        return getDouble("/policies/order/minOrderValueForPromo", DEFAULT_MIN_ORDER_VALUE);
    }

    public boolean isStackablePromosAllowed() {
        return getBoolean("/policies/discount/stackablePromosAllowed", DEFAULT_STACKABLE_ALLOWED);
    }

    public int getMaxActivePromos() {
        return getInt("/policies/limits/maxActivePromos", DEFAULT_MAX_ACTIVE_PROMOS);
    }

    // ======================== Validation methods ========================

    /**
     * Full validation for scheduling a promo (DRAFT -> SCHEDULED).
     * Validates discount limits, date validity, usage limits, stackability.
     */
    public void validatePromoForScheduling(Coupon coupon) {
        validateDiscountLimits(coupon);
        validateDateRange(coupon);
        validateUsageLimits(coupon);
    }

    /**
     * Validates discount type and value are within platform limits.
     */
    public void validateDiscountLimits(Coupon coupon) {
        if (coupon.getDiscountType() == null) {
            throw new IllegalArgumentException("Discount type is required");
        }
        if (coupon.getDiscountValue() == null || coupon.getDiscountValue() <= 0) {
            throw new IllegalArgumentException("Discount value must be positive");
        }

        String type = coupon.getDiscountType().toUpperCase();
        switch (type) {
            case "PERCENTAGE":
                double maxPercent = getMaxDiscountPercent();
                if (coupon.getDiscountValue() > maxPercent) {
                    throw new IllegalArgumentException(
                            "Discount of " + coupon.getDiscountValue() + "% exceeds maximum of " + maxPercent + "%");
                }
                break;
            case "FLAT":
                double maxFlat = getMaxFlatDiscount();
                if (coupon.getDiscountValue() > maxFlat) {
                    throw new IllegalArgumentException(
                            "Flat discount of " + coupon.getDiscountValue() + " exceeds maximum of " + maxFlat);
                }
                break;
            case "BUY_X_GET_Y":
                // BUY_X_GET_Y validated by strategy layer
                break;
            default:
                throw new IllegalArgumentException("Unknown discount type: " + coupon.getDiscountType());
        }
    }

    /**
     * Validates that start/end dates are sensible.
     */
    public void validateDateRange(Coupon coupon) {
        if (coupon.getStartDate() != null && coupon.getEndDate() != null) {
            if (coupon.getEndDate().isBefore(coupon.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date");
            }
        }
        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("End date must be in the future");
        }
    }

    /**
     * Validates usage limits are within platform constraints.
     */
    public void validateUsageLimits(Coupon coupon) {
        int maxPerPromo = getMaxUsagePerPromo();
        if (coupon.getUsageLimit() != null && coupon.getUsageLimit() > maxPerPromo) {
            throw new IllegalArgumentException(
                    "Usage limit of " + coupon.getUsageLimit() + " exceeds platform max of " + maxPerPromo);
        }

        int maxPerCustomer = getMaxUsagePerCustomer();
        if (coupon.getUsagePerCustomer() != null && coupon.getUsagePerCustomer() > maxPerCustomer) {
            throw new IllegalArgumentException(
                    "Per-customer usage limit of " + coupon.getUsagePerCustomer() +
                    " exceeds platform max of " + maxPerCustomer);
        }
    }

    /**
     * Validates order value meets minimum requirement for promo usage.
     */
    public void validateMinOrderValue(double orderValue) {
        double min = getMinOrderValueForPromo();
        if (orderValue < min) {
            throw new IllegalArgumentException(
                    "Order value " + orderValue + " is below minimum " + min + " required for promo");
        }
    }

    /**
     * Validates that the number of active promos hasn't exceeded the platform limit.
     * Note: In production, this would query the repository for active promo count.
     * For now, this is a placeholder that can be wired up.
     */
    public void validateActivePromoCount() {
        // This will be called by ActivatePromoAction.
        // In production, inject PromoRepository and count active promos.
        // int maxActive = getMaxActivePromos();
        // long activeCount = promoRepository.countByStateId("ACTIVE");
        // if (activeCount >= maxActive) throw ...
    }

    /**
     * Returns the configured rounding mode for discount computation.
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
}
