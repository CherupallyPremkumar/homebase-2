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
 * UserRegisteredHook -- triggered when user enters REGISTERED state.
 *
 * Publishes USER_REGISTERED to user.events and triggers verification email
 * via the event publisher (which the infrastructure translates to
 * Keycloak verification email + Kafka event).
 */
public class REGISTEREDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(REGISTEREDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public REGISTEREDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String event = (String) user.getTransientMap().get("event");

        if ("REGISTERED".equals(event) || "VERIFICATION_RESENT".equals(event)) {
            log.info("User {} registered with email {}. Sending verification email.", user.getId(), user.getEmail());

            eventPublisher.publishEmailVerificationRequested(
                    new UserDomainEvents.EmailVerificationRequested(
                            user.getId(), user.getEmail(), Instant.now()
                    )
            );
        }
    }
}
