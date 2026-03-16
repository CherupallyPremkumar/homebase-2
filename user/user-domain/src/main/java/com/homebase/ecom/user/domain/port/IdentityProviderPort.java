package com.homebase.ecom.user.domain.port;

/**
 * Outbound Port (Hexagonal): Identity provider operations.
 * Abstraction over Keycloak for user identity management.
 * Infrastructure provides the implementation.
 */
public interface IdentityProviderPort {

    /** Send email verification link to the user. */
    void sendVerificationEmail(String userId, String email);

    /** Revoke all active sessions for a user. */
    void revokeAllSessions(String userId);

    /** Disable user in the identity provider. */
    void disableUser(String userId);

    /** Enable user in the identity provider. */
    void enableUser(String userId);
}
