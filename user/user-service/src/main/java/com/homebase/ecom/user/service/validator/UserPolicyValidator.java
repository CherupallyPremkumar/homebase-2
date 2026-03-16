package com.homebase.ecom.user.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * UserPolicyValidator -- enforces all user account policies and business rules.
 *
 * Policies (from user.json via CconfigClient):
 *  - Email format validation
 *  - Password strength (min length from config, default 8)
 *  - Login attempt limits (maxLoginAttempts, default 5)
 *  - Address count limits (maxAddressesPerUser, default 10)
 *  - Lockout duration (lockoutDurationMinutes, default 30)
 *  - Email verification timeout (emailVerificationTimeoutHours, default 48)
 *  - KYC requirement for seller (kycRequiredForSeller, default true)
 *
 * No @Component -- wired in UserConfiguration.
 */
public class UserPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(UserPolicyValidator.class);

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public UserPolicyValidator() {}

    public void setCconfigClient(CconfigClient cconfigClient) {
        this.cconfigClient = cconfigClient;
    }

    private JsonNode getUserConfig() {
        if (cconfigClient == null) return mapper.createObjectNode();
        try {
            java.util.Map<String, Object> map = cconfigClient.value("user", null);
            if (map != null) return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load user.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ── Email validation ─────────────────────────────────────────────────

    /** Validates email format. Throws IllegalArgumentException if invalid. */
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    // ── Password strength ────────────────────────────────────────────────

    /** Validates password meets minimum length. */
    public void validatePasswordStrength(String password) {
        int minLength = getPasswordMinLength();
        if (password == null || password.length() < minLength) {
            throw new IllegalArgumentException("Password must be at least " + minLength + " characters");
        }
    }

    public int getPasswordMinLength() {
        JsonNode node = getUserConfig().at("/passwordMinLength");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 8;
    }

    // ── Login attempt limits ─────────────────────────────────────────────

    /** Returns max allowed failed login attempts before lockout. */
    public int getMaxLoginAttempts() {
        JsonNode node = getUserConfig().at("/maxLoginAttempts");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    /** Validates that the user has not exceeded the max login attempts. */
    public void validateLoginAttempts(int currentAttempts) {
        int max = getMaxLoginAttempts();
        if (currentAttempts >= max) {
            throw new IllegalStateException(
                    "Account locked: " + currentAttempts + " failed attempts (max " + max + ")");
        }
    }

    // ── Lockout duration ─────────────────────────────────────────────────

    /** Returns account lockout duration in minutes. */
    public int getLockoutDurationMinutes() {
        JsonNode node = getUserConfig().at("/lockoutDurationMinutes");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    // ── Address limits ───────────────────────────────────────────────────

    /** Returns max addresses per user. */
    public int getMaxAddressesPerUser() {
        JsonNode node = getUserConfig().at("/maxAddressesPerUser");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 10;
    }

    /** Validates that the user has not exceeded the max address count. */
    public void validateAddressCount(int currentAddressCount) {
        int max = getMaxAddressesPerUser();
        if (currentAddressCount >= max) {
            throw new IllegalStateException(
                    "Maximum " + max + " addresses allowed. Remove an existing address first.");
        }
    }

    // ── Email verification timeout ───────────────────────────────────────

    /** Returns email verification timeout in hours. */
    public int getEmailVerificationTimeoutHours() {
        JsonNode node = getUserConfig().at("/emailVerificationTimeoutHours");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 48;
    }

    // ── KYC requirement ──────────────────────────────────────────────────

    /** Returns whether KYC is required for seller role. */
    public boolean isKycRequiredForSeller() {
        JsonNode node = getUserConfig().at("/kycRequiredForSeller");
        return node.isMissingNode() || node.asBoolean(true);
    }
}
