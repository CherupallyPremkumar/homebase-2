package com.homebase.ecom.offer.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Policy validator for offer operations. Reads configurable thresholds
 * from cconfig (org/chenile/config/offer.json) with fallback defaults.
 *
 * Validates:
 * - Discount limits (maxDiscountPercent)
 * - Margin check (minMarginPercent)
 * - Duration limits (offerDurationMaxDays)
 * - Max offers per product (maxOffersPerProduct)
 * - Seller rating for auto-approval
 */
@Component
public class OfferPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(OfferPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Discount limits
    // ═══════════════════════════════════════════════════════════════════════

    public void validateDiscountLimit(Offer offer) {
        JsonNode config = getOfferConfig();
        int maxDiscount = configInt(config, "/policies/discount/maxDiscountPercent", 70);

        if (offer.getDiscountPercent() != null &&
                offer.getDiscountPercent().compareTo(BigDecimal.valueOf(maxDiscount)) > 0) {
            throw new IllegalArgumentException(
                    "Discount percent " + offer.getDiscountPercent() +
                    "% exceeds maximum allowed " + maxDiscount + "%");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Margin check
    // ═══════════════════════════════════════════════════════════════════════

    public void validateMargin(Offer offer) {
        JsonNode config = getOfferConfig();
        int minMargin = configInt(config, "/policies/margin/minMarginPercent", 5);

        BigDecimal marginPercent = offer.computeMarginPercent();
        if (marginPercent.compareTo(BigDecimal.valueOf(minMargin)) < 0) {
            throw new IllegalArgumentException(
                    "Margin " + marginPercent + "% is below minimum required " + minMargin + "%");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Duration limits
    // ═══════════════════════════════════════════════════════════════════════

    public void validateDuration(Offer offer) {
        JsonNode config = getOfferConfig();
        int maxDays = configInt(config, "/policies/offer/offerDurationMaxDays", 90);

        if (offer.getStartDate() != null && offer.getEndDate() != null) {
            long diffMillis = offer.getEndDate().getTime() - offer.getStartDate().getTime();
            long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
            if (diffDays > maxDays) {
                throw new IllegalArgumentException(
                        "Offer duration " + diffDays + " days exceeds maximum allowed " + maxDays + " days");
            }
            if (diffDays < 0) {
                throw new IllegalArgumentException("End date must be after start date");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Max offers per product
    // ═══════════════════════════════════════════════════════════════════════

    public void validateMaxOffersPerProduct(String productId, int currentActiveCount) {
        JsonNode config = getOfferConfig();
        int maxOffers = configInt(config, "/policies/offer/maxOffersPerProduct", 3);

        if (currentActiveCount >= maxOffers) {
            throw new IllegalArgumentException(
                    "Product " + productId + " already has " + currentActiveCount +
                    " active offers (max: " + maxOffers + ")");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Seller rating check
    // ═══════════════════════════════════════════════════════════════════════

    public boolean isEligibleForAutoApproval(Offer offer) {
        JsonNode config = getOfferConfig();
        double autoApproveRating = configDouble(config, "/policies/approval/autoApproveSellerRating", 4.5);
        int requireApprovalAboveDiscount = configInt(config, "/policies/discount/requireApprovalAboveDiscount", 30);

        boolean discountOk = offer.getDiscountPercent() == null ||
                offer.getDiscountPercent().doubleValue() <= requireApprovalAboveDiscount;
        boolean ratingOk = offer.getSellerRating() != null &&
                offer.getSellerRating().doubleValue() >= autoApproveRating;

        return discountOk && ratingOk;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // COMPOSITE: Validate all policies for submission
    // ═══════════════════════════════════════════════════════════════════════

    public void validateForSubmission(Offer offer) {
        validateDiscountLimit(offer);
        validateMargin(offer);
        validateDuration(offer);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getOfferConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("offer", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load offer.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private double configDouble(JsonNode config, String path, double defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : defaultVal;
    }
}
