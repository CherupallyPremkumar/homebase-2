package com.homebase.ecom.user.infrastructure.adapter;

import com.homebase.ecom.user.domain.port.IdentityProviderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for IdentityProviderPort.
 * Delegates to Keycloak Admin API for identity operations.
 * Currently logs operations; real Keycloak integration added when
 * keycloak-admin-client dependency is configured.
 *
 * No @Component -- wired in UserConfiguration.
 */
public class IdentityProviderAdapter implements IdentityProviderPort {

    private static final Logger log = LoggerFactory.getLogger(IdentityProviderAdapter.class);

    @Override
    public void sendVerificationEmail(String userId, String email) {
        log.info("Identity provider: sending verification email to {} for user {}", email, userId);
        // TODO: Keycloak Admin API — trigger verify-email required action
    }

    @Override
    public void revokeAllSessions(String userId) {
        log.info("Identity provider: revoking all sessions for user {}", userId);
        // TODO: Keycloak Admin API — DELETE /users/{id}/sessions
    }

    @Override
    public void disableUser(String userId) {
        log.info("Identity provider: disabling user {}", userId);
        // TODO: Keycloak Admin API — PUT /users/{id} with enabled=false
    }

    @Override
    public void enableUser(String userId) {
        log.info("Identity provider: enabling user {}", userId);
        // TODO: Keycloak Admin API — PUT /users/{id} with enabled=true
    }
}
