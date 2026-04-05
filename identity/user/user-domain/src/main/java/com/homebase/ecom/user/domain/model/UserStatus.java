package com.homebase.ecom.user.domain.model;

/**
 * UserStatus -- the business states of a User in the STM lifecycle.
 * Maps directly to states in user-states.xml.
 *
 * Main path: REGISTERED -> EMAIL_VERIFIED -> ACTIVE -> SUSPENDED -> DEACTIVATED
 * KYC path: KYC_PENDING -> KYC_VERIFIED
 * Auto-state: CHECK_VERIFICATION_TIMEOUT
 */
public enum UserStatus {
    /** Just registered, verification email sent. */
    REGISTERED,
    /** Email verified, profile completion in progress. */
    EMAIL_VERIFIED,
    /** Fully active user. */
    ACTIVE,
    /** Locked after too many failed login attempts. */
    LOCKED,
    /** Suspended by admin action (fraud, policy violation). */
    SUSPENDED,
    /** Account deactivated (soft-delete). */
    DEACTIVATED,
    /** KYC pending -- seller must submit KYC docs. */
    KYC_PENDING,
    /** KYC verified by admin. */
    KYC_VERIFIED
}
