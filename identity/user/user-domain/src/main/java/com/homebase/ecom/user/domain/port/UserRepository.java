package com.homebase.ecom.user.domain.port;

import com.homebase.ecom.user.domain.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Outbound Port (Hexagonal): User persistence.
 *
 * Domain depends on this interface. JPA implementation lives in user-infrastructure.
 * No Spring/JPA annotations here.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    /** Primary lookup for /api/users/me — from Keycloak JWT `sub` claim. */
    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByEmail(String email);

    /**
     * Find PENDING_VERIFICATION users older than cutoff.
     * Used by scheduler to delete unverified accounts after 7 days.
     */
    List<User> findPendingVerificationOlderThan(Instant cutoff);

    void delete(String id);
}
