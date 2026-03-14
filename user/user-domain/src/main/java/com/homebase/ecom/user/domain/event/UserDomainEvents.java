package com.homebase.ecom.user.domain.event;

import java.time.Instant;

/**
 * Domain Events published by the User BC.
 *
 * These are records (immutable) that describe facts that happened in the domain.
 * The Event Management module (infra) translates these to the event bus.
 *
 * Events are versioned via the class name and must remain backward-compatible.
 */
public final class UserDomainEvents {

    private UserDomainEvents() {}

    // ─── Registration & Verification ───────────────────────────────────────

    public record UserProfileCreated(
            String userId,
            String keycloakId,
            String email,
            Instant createdAt
    ) {}

    public record EmailVerificationRequested(
            String userId,
            String email,
            Instant requestedAt
    ) {}

    public record UserActivated(
            String userId,
            String keycloakId,
            Instant activatedAt
    ) {}

    public record UnverifiedUserDeleted(
            String userId,
            String email,
            Instant deletedAt,
            String reason   // e.g. "Unverified after 7 days"
    ) {}

    // ─── Profile & Preferences ─────────────────────────────────────────────

    public record ProfileUpdated(
            String userId,
            Instant updatedAt
    ) {}

    public record AddressAdded(
            String userId,
            String addressId,
            boolean isDefault,
            Instant addedAt
    ) {}

    public record AddressRemoved(
            String userId,
            String addressId,
            Instant removedAt
    ) {}

    public record DefaultAddressChanged(
            String userId,
            String newDefaultAddressId,
            Instant changedAt
    ) {}

    public record PreferencesUpdated(
            String userId,
            String newCurrency,
            String newLanguage,
            Instant updatedAt
    ) {}

    // ─── Account Status Changes ────────────────────────────────────────────

    public record AccountLocked(
            String userId,
            String reason,
            int failedAttempts,
            Instant lockedAt
    ) {}

    public record AccountUnlocked(
            String userId,
            String unlockedBy,   // "ADMIN" or "TIMEOUT"
            Instant unlockedAt
    ) {}

    public record UserSuspended(
            String userId,
            String reason,
            String suspendedBy,  // admin ID
            Instant suspendedAt
    ) {}

    public record UserReinstated(
            String userId,
            String reinstatedBy, // admin ID
            Instant reinstatedAt
    ) {}

    public record UserDeleted(
            String userId,
            String email,
            String deletedBy,    // "SELF", "ADMIN"
            Instant deletedAt
    ) {}
}
