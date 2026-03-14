package com.homebase.ecom.user.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Central component enforcing all user account policies and rules.
 *
 * <h3>Policies (enforce constraints)</h3>
 * <ul>
 * <li>Email/phone verification requirements</li>
 * <li>Maximum active addresses</li>
 * <li>Account lockout after failed login attempts</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Wishlist item limit</li>
 * <li>Loyalty points: earn rate, redemption threshold, expiry</li>
 * <li>Notification preferences</li>
 * </ul>
 *
 * All values sourced from {@code user.json} via {@code CconfigClient}.
 */
@Component
public class UserPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(UserPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getUserConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("user", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load user.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Account
    // ===========================================================

    /**
     * Returns true if email verification is required on signup.
     * Controlled by: user.json → policies.account.requireEmailVerification
     */
    public boolean isEmailVerificationRequired() {
        JsonNode node = getUserConfig().at("/policies/account/requireEmailVerification");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /**
     * Returns true if phone verification is required on signup.
     * Controlled by: user.json → policies.account.requirePhoneVerification
     */
    public boolean isPhoneVerificationRequired() {
        JsonNode node = getUserConfig().at("/policies/account/requirePhoneVerification");
        return node.isMissingNode() || node.asBoolean(true);
    }

    /**
     * Validates that the user has not exceeded the maximum allowed addresses.
     * Controlled by: user.json → policies.account.maxActiveAddresses
     */
    public void validateAddressCount(int currentAddressCount) {
        JsonNode node = getUserConfig().at("/policies/account/maxActiveAddresses");
        int max = (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
        if (currentAddressCount >= max) {
            throw new IllegalStateException(
                    "Maximum " + max + " saved addresses allowed. Remove an existing address first.");
        }
    }

    // ===========================================================
    // POLICY: Security
    // ===========================================================

    /** Returns max allowed failed login attempts before lockout. */
    public int getMaxFailedLoginAttempts() {
        JsonNode node = getUserConfig().at("/policies/security/maxFailedLoginAttempts");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    /** Returns account lockout duration in minutes. */
    public int getLockoutDurationMinutes() {
        JsonNode node = getUserConfig().at("/policies/security/lockoutDurationMinutes");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    /** Returns session timeout duration in minutes. */
    public int getSessionTimeoutMinutes() {
        JsonNode node = getUserConfig().at("/policies/security/sessionTimeoutMinutes");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 120;
    }

    // ===========================================================
    // RULE: Wishlist
    // ===========================================================

    /** Returns maximum wishlist items per user. */
    public int getMaxWishlistItems() {
        JsonNode node = getUserConfig().at("/rules/wishlist/maxWishlistItems");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 100;
    }

    // ===========================================================
    // RULE: Loyalty
    // ===========================================================

    /** Returns points earned per rupee spent. */
    public int getLoyaltyPointsPerRupee() {
        JsonNode node = getUserConfig().at("/rules/loyalty/pointsPerRupee");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 1;
    }

    /** Returns minimum points required for redemption. */
    public int getLoyaltyRedemptionThreshold() {
        JsonNode node = getUserConfig().at("/rules/loyalty/redemptionThresholdPoints");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 1000;
    }

    /** Returns loyalty point expiry in days. */
    public int getLoyaltyPointExpiryDays() {
        JsonNode node = getUserConfig().at("/rules/loyalty/pointExpiryDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 365;
    }
}
