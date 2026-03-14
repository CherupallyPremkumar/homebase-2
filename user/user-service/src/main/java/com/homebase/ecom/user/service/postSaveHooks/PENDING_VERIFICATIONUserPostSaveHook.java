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
 * Triggered when user enters or remains in PENDING_VERIFICATION state.
 *
 * Covers two scenarios:
 *   1. New registration  -> request initial verification email
 *   2. Resend verification -> re-request verification email
 *
 * Both publish an EmailVerificationRequested event that the infrastructure
 * layer translates into a Keycloak verification email trigger.
 */
public class PENDING_VERIFICATIONUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(PENDING_VERIFICATIONUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public PENDING_VERIFICATIONUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String event = (String) user.getTransientMap().get("event");

        if ("REGISTERED".equals(event) || "VERIFICATION_RESENT".equals(event)) {
            log.info("Requesting email verification for user {}, email {}", user.getId(), user.getEmail());

            eventPublisher.publishEmailVerificationRequested(
                    new UserDomainEvents.EmailVerificationRequested(
                            user.getId(), user.getEmail(), Instant.now()
                    )
            );
        }
    }
}
