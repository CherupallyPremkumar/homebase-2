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
 * Triggered when account enters LOCKED state.
 * Publishes AccountLocked event so downstream can notify the user
 * via email/SMS and log the security incident.
 */
public class LOCKEDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(LOCKEDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public LOCKEDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String reason = (String) user.getTransientMap().getOrDefault("reason", user.getLockReason());
        int failedAttempts = user.getFailedLoginAttempts();

        log.info("User {} locked. Reason: {}, Failed attempts: {}", user.getId(), reason, failedAttempts);

        eventPublisher.publishAccountLocked(
                new UserDomainEvents.AccountLocked(user.getId(), reason, failedAttempts, Instant.now())
        );
    }
}
