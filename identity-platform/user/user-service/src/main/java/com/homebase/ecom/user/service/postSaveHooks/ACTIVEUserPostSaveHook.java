package com.homebase.ecom.user.service.postSaveHooks;

import com.homebase.ecom.user.domain.event.UserDomainEvents;
import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.port.UserEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Triggered when user enters ACTIVE state.
 *
 * This can happen from:
 *   - EMAIL_VERIFIED (system activation) -> publishes UserActivated
 *   - LOCKED (account unlocked)          -> publishes AccountUnlocked
 *   - SUSPENDED (user reinstated)        -> publishes UserReinstated
 *   - Self-transitions (profile, address) -> publishes appropriate events
 */
public class ACTIVEUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(ACTIVEUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public ACTIVEUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String event = (String) user.getTransientMap().get("event");
        if (event == null) return;

        Instant now = Instant.now();

        switch (event) {
            case "USER_ACTIVATED" -> {
                log.info("User {} activated", user.getId());
                eventPublisher.publishUserActivated(
                        new UserDomainEvents.UserActivated(user.getId(), user.getKeycloakId(), now)
                );
            }
            case "ACCOUNT_UNLOCKED" -> {
                String unlockedBy = (String) user.getTransientMap().getOrDefault("unlockedBy", "ADMIN");
                log.info("User {} account unlocked by {}", user.getId(), unlockedBy);
                eventPublisher.publishAccountUnlocked(
                        new UserDomainEvents.AccountUnlocked(user.getId(), unlockedBy, now)
                );
            }
            case "USER_REINSTATED" -> {
                log.info("User {} reinstated by admin", user.getId());
                eventPublisher.publishUserReinstated(
                        new UserDomainEvents.UserReinstated(user.getId(), "ADMIN", now)
                );
            }
            case "PROFILE_UPDATED" -> {
                log.info("User {} profile updated", user.getId());
                eventPublisher.publishProfileUpdated(
                        new UserDomainEvents.ProfileUpdated(user.getId(), now)
                );
            }
            case "ADDRESS_ADDED" -> {
                String addressId = (String) user.getTransientMap().get("addressId");
                boolean isDefault = Boolean.parseBoolean(
                        (String) user.getTransientMap().getOrDefault("isDefault", "false"));
                log.info("User {} added address {}", user.getId(), addressId);
                eventPublisher.publishAddressAdded(
                        new UserDomainEvents.AddressAdded(user.getId(), addressId, isDefault, now)
                );
            }
            case "ADDRESS_REMOVED" -> {
                String addressId = (String) user.getTransientMap().get("addressId");
                log.info("User {} removed address {}", user.getId(), addressId);
                eventPublisher.publishAddressRemoved(
                        new UserDomainEvents.AddressRemoved(user.getId(), addressId, now)
                );
            }
            default -> log.debug("ACTIVE post-save hook: unhandled event type '{}'", event);
        }
    }
}
