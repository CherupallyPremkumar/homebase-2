package com.homebase.ecom.user.domain.model;

/**
 * UserStatus — the 5 business states of a User in the STM lifecycle.
 *
 * These map directly to states in user-states.xml.
 * Auth states (tokens, sessions) are owned by Keycloak — not here.
 */
public enum UserStatus {
    /** User registered but email not yet verified. Deleted after 7 days if not verified. */
    PENDING_VERIFICATION,
    /** Fully active — can shop, place orders, manage profile. */
    ACTIVE,
    /** Locked after 3 consecutive failed logins. Unlocked by admin or timeout. */
    LOCKED,
    /** Suspended by admin action (e.g. fraud, policy violation). */
    SUSPENDED,
    /** Permanently deleted — self-initiated or admin hard-delete. */
    DELETED
}
