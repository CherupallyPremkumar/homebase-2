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
 * UserVerifiedHook -- triggered when user enters EMAIL_VERIFIED state.
 *
 * Publishes USER_VERIFIED to user.events. Downstream services
 * use this to notify the user and enable features.
 */
public class EMAIL_VERIFIEDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(EMAIL_VERIFIEDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public EMAIL_VERIFIEDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        log.info("User {} email verified. Publishing USER_VERIFIED event.", user.getId());

        eventPublisher.publishUserActivated(
                new UserDomainEvents.UserActivated(user.getId(), user.getKeycloakId(), Instant.now())
        );
    }
}
