package com.homebase.ecom.user.domain.event;

import java.time.Instant;

/**
 * Domain Events published by the User BC.
 *
 * These are records (immutable) that describe facts that happened in the domain.
 * Published to user.events: USER_REGISTERED, USER_VERIFIED, USER_SUSPENDED, USER_DEACTIVATED.
 */
public final class UserDomainEvents {

    private UserDomainEvents() {}

    // --- Registration and Verification ---

    public record UserProfileCreated(
            String userId,
            String keycloakId,
            String email,
            String role,
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

    // --- Profile and Addresses ---

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

    // --- Account Status Changes ---

    public record AccountLocked(
            String userId,
            String reason,
            int failedAttempts,
            Instant lockedAt
    ) {}

    public record AccountUnlocked(
            String userId,
            String unlockedBy,
            Instant unlockedAt
    ) {}

    public record UserSuspended(
            String userId,
            String reason,
            String suspendedBy,
            Instant suspendedAt
    ) {}

    public record UserReinstated(
            String userId,
            String reinstatedBy,
            Instant reinstatedAt
    ) {}

    public record UserDeleted(
            String userId,
            String email,
            String deletedBy,
            Instant deletedAt
    ) {}

    // --- KYC ---

    public record KycSubmitted(
            String userId,
            String documentType,
            Instant submittedAt
    ) {}

    public record KycVerified(
            String userId,
            Instant verifiedAt
    ) {}
}
