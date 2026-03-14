package com.homebase.ecom.user.domain.port;

import com.homebase.ecom.user.domain.event.UserDomainEvents;

/**
 * Outbound Port (Hexagonal): Domain event publication.
 *
 * The domain/service layer depends on this interface. Infrastructure provides
 * the implementation (e.g. Spring ApplicationEventPublisher, Kafka, etc.).
 *
 * Follows the same pattern as CartEventPublisher in the cart BC.
 */
public interface UserEventPublisher {

    void publishUserActivated(UserDomainEvents.UserActivated event);

    void publishAccountLocked(UserDomainEvents.AccountLocked event);

    void publishAccountUnlocked(UserDomainEvents.AccountUnlocked event);

    void publishUserSuspended(UserDomainEvents.UserSuspended event);

    void publishUserReinstated(UserDomainEvents.UserReinstated event);

    void publishUserDeleted(UserDomainEvents.UserDeleted event);

    void publishEmailVerificationRequested(UserDomainEvents.EmailVerificationRequested event);

    void publishProfileUpdated(UserDomainEvents.ProfileUpdated event);

    void publishAddressAdded(UserDomainEvents.AddressAdded event);

    void publishAddressRemoved(UserDomainEvents.AddressRemoved event);
}
