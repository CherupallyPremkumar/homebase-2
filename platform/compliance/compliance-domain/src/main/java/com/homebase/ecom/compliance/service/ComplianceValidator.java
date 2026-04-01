package com.homebase.ecom.compliance.service;

import java.time.LocalDate;

import org.chenile.base.exception.BadRequestException;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.model.PlatformPolicy;

/**
 * Validates compliance entities using Chenile i18n error codes.
 * Error codes 7001-7020 are reserved for compliance module.
 * Messages resolved from messages.properties via Chenile's MultipleMessageSource.
 */
public class ComplianceValidator {

    // ================================================================
    // Error Code Constants (compliance range: 10001–10099)
    // Each module gets its own 10000-range block.
    // Resolved via messages.properties → E10001, E10002, etc.
    // ================================================================

    public static final int AGREEMENT_TITLE_REQUIRED = 10001;
    public static final int AGREEMENT_TITLE_TOO_LONG = 10002;
    public static final int AGREEMENT_TYPE_REQUIRED = 10003;
    public static final int AGREEMENT_CONTENT_URL_REQUIRED = 10004;
    public static final int AGREEMENT_VERSION_LABEL_REQUIRED = 10005;
    public static final int AGREEMENT_EFFECTIVE_DATE_REQUIRED = 10006;
    public static final int AGREEMENT_CONTENT_HASH_REQUIRED = 10007;
    public static final int AGREEMENT_EFFECTIVE_DATE_LEAD_TIME = 10008;
    public static final int AGREEMENT_SUPERSEDED_BY_REQUIRED = 10009;
    public static final int COMMENT_REQUIRED = 10010;
    public static final int POLICY_TITLE_REQUIRED = 10011;
    public static final int POLICY_TITLE_TOO_LONG = 10012;
    public static final int POLICY_CATEGORY_REQUIRED = 10013;
    public static final int POLICY_CONTENT_URL_REQUIRED = 10014;
    public static final int POLICY_VERSION_LABEL_REQUIRED = 10015;
    public static final int POLICY_SUMMARY_REQUIRED = 10016;
    public static final int POLICY_SUMMARY_TOO_LONG = 10017;
    public static final int POLICY_EFFECTIVE_DATE_LEAD_TIME = 10018;
    public static final int POLICY_AMEND_REASON_REQUIRED = 10019;

    private final int agreementMinLeadDays;
    private final int policyMinLeadDays;
    private final int agreementTitleMaxLength;
    private final int policyTitleMaxLength;
    private final int policySummaryMaxLength;

    public ComplianceValidator(int agreementMinLeadDays, int policyMinLeadDays,
                               int agreementTitleMaxLength, int policyTitleMaxLength,
                               int policySummaryMaxLength) {
        this.agreementMinLeadDays = agreementMinLeadDays;
        this.policyMinLeadDays = policyMinLeadDays;
        this.agreementTitleMaxLength = agreementTitleMaxLength;
        this.policyTitleMaxLength = policyTitleMaxLength;
        this.policySummaryMaxLength = policySummaryMaxLength;
    }

    // ================================================================
    // Agreement Validations
    // ================================================================

    public void validateAgreementForCreate(Agreement agreement) {
        requireNonBlank(agreement.getTitle(), AGREEMENT_TITLE_REQUIRED);
        requireMaxLength(agreement.getTitle(), agreementTitleMaxLength,
                AGREEMENT_TITLE_TOO_LONG, agreementTitleMaxLength);
        requireNonBlank(agreement.getAgreementType(), AGREEMENT_TYPE_REQUIRED);
    }

    public void validateAgreementForPublish(Agreement agreement) {
        requireNonBlank(agreement.getContentUrl(), AGREEMENT_CONTENT_URL_REQUIRED);
        requireNonBlank(agreement.getVersionLabel(), AGREEMENT_VERSION_LABEL_REQUIRED);
        requireNonNull(agreement.getEffectiveDate(), AGREEMENT_EFFECTIVE_DATE_REQUIRED);
        requireNonBlank(agreement.getContentHash(), AGREEMENT_CONTENT_HASH_REQUIRED);
        requireMinLeadDate(agreement.getEffectiveDate(), agreementMinLeadDays,
                AGREEMENT_EFFECTIVE_DATE_LEAD_TIME, agreementMinLeadDays);
    }

    public void validateAgreementForSupersede(Agreement agreement) {
        requireNonBlank(agreement.getSupersededById(), AGREEMENT_SUPERSEDED_BY_REQUIRED);
    }

    public void validateCommentRequired(String comment, String eventName) {
        if (comment == null || comment.isBlank()) {
            throw new BadRequestException(COMMENT_REQUIRED, new Object[]{eventName});
        }
    }

    // ================================================================
    // Platform Policy Validations
    // ================================================================

    public void validatePolicyForCreate(PlatformPolicy policy) {
        requireNonBlank(policy.getTitle(), POLICY_TITLE_REQUIRED);
        requireMaxLength(policy.getTitle(), policyTitleMaxLength,
                POLICY_TITLE_TOO_LONG, policyTitleMaxLength);
        requireNonBlank(policy.getPolicyCategory(), POLICY_CATEGORY_REQUIRED);
    }

    public void validatePolicyForPublish(PlatformPolicy policy) {
        requireNonBlank(policy.getContentUrl(), POLICY_CONTENT_URL_REQUIRED);
        requireNonBlank(policy.getVersionLabel(), POLICY_VERSION_LABEL_REQUIRED);
        requireNonBlank(policy.getSummaryText(), POLICY_SUMMARY_REQUIRED);
        requireMaxLength(policy.getSummaryText(), policySummaryMaxLength,
                POLICY_SUMMARY_TOO_LONG, policySummaryMaxLength);
        if (policy.getEffectiveDate() != null) {
            requireMinLeadDate(policy.getEffectiveDate(), policyMinLeadDays,
                    POLICY_EFFECTIVE_DATE_LEAD_TIME, policyMinLeadDays);
        }
    }

    public void validatePolicyAmendReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BadRequestException(POLICY_AMEND_REASON_REQUIRED, new Object[]{});
        }
    }

    // ================================================================
    // Config Getters (for frontend API consumption)
    // ================================================================

    public int getAgreementMinLeadDays() { return agreementMinLeadDays; }
    public int getPolicyMinLeadDays() { return policyMinLeadDays; }
    public int getAgreementTitleMaxLength() { return agreementTitleMaxLength; }
    public int getPolicyTitleMaxLength() { return policyTitleMaxLength; }
    public int getPolicySummaryMaxLength() { return policySummaryMaxLength; }

    // ================================================================
    // Private Helpers — throw BadRequestException with i18n subErrorCode
    // ================================================================

    private void requireNonBlank(String value, int errorCode) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(errorCode, new Object[]{});
        }
    }

    private void requireNonNull(Object value, int errorCode) {
        if (value == null) {
            throw new BadRequestException(errorCode, new Object[]{});
        }
    }

    private void requireMaxLength(String value, int maxLength, int errorCode, int maxLengthParam) {
        if (value != null && value.length() > maxLength) {
            throw new BadRequestException(errorCode, new Object[]{maxLengthParam});
        }
    }

    private void requireMinLeadDate(LocalDate date, int minDays, int errorCode, int minDaysParam) {
        if (date != null && date.isBefore(LocalDate.now().plusDays(minDays))) {
            throw new BadRequestException(errorCode, new Object[]{minDaysParam});
        }
    }
}
