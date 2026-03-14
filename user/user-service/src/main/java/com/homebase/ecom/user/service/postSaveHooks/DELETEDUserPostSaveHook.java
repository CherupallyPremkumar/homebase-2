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
 * Triggered when account enters DELETED state.
 * Publishes UserDeleted event so downstream can:
 *   - Revoke all Keycloak sessions
 *   - Schedule full data anonymization job
 *   - Archive order history references
 */
public class DELETEDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(DELETEDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public DELETEDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String deletedBy = (String) user.getTransientMap().getOrDefault("deletedBy", "SELF");
        String originalEmail = (String) user.getTransientMap().getOrDefault("originalEmail", "unknown");

        log.info("User {} deleted by {}. Original email: {}", user.getId(), deletedBy, originalEmail);

        eventPublisher.publishUserDeleted(
                new UserDomainEvents.UserDeleted(user.getId(), originalEmail, deletedBy, Instant.now())
        );
    }
}
