package com.homebase.ecom.user.infrastructure.event;

import com.homebase.ecom.user.domain.event.UserDomainEvents;
import com.homebase.ecom.user.domain.port.UserEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Infrastructure implementation of UserEventPublisher.
 * Publishes domain events via Spring's ApplicationEventPublisher.
 * No @Component — wired explicitly in UserConfiguration.
 */
public class UserEventPublisherImpl implements UserEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(UserEventPublisherImpl.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishUserActivated(UserDomainEvents.UserActivated event) {
        log.info("Publishing UserActivated event for userId={}", event.userId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAccountLocked(UserDomainEvents.AccountLocked event) {
        log.info("Publishing AccountLocked event for userId={}, reason={}", event.userId(), event.reason());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAccountUnlocked(UserDomainEvents.AccountUnlocked event) {
        log.info("Publishing AccountUnlocked event for userId={}", event.userId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishUserSuspended(UserDomainEvents.UserSuspended event) {
        log.info("Publishing UserSuspended event for userId={}, reason={}", event.userId(), event.reason());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishUserReinstated(UserDomainEvents.UserReinstated event) {
        log.info("Publishing UserReinstated event for userId={}", event.userId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishUserDeleted(UserDomainEvents.UserDeleted event) {
        log.info("Publishing UserDeleted event for userId={}", event.userId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishEmailVerificationRequested(UserDomainEvents.EmailVerificationRequested event) {
        log.info("Publishing EmailVerificationRequested event for userId={}, email={}", event.userId(), event.email());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishProfileUpdated(UserDomainEvents.ProfileUpdated event) {
        log.info("Publishing ProfileUpdated event for userId={}", event.userId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAddressAdded(UserDomainEvents.AddressAdded event) {
        log.info("Publishing AddressAdded event for userId={}, addressId={}", event.userId(), event.addressId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAddressRemoved(UserDomainEvents.AddressRemoved event) {
        log.info("Publishing AddressRemoved event for userId={}, addressId={}", event.userId(), event.addressId());
        applicationEventPublisher.publishEvent(event);
    }
}
