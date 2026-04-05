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
 * UserSuspendedHook -- triggered when user enters SUSPENDED state.
 * Publishes USER_SUSPENDED event so downstream can:
 *   - Notify the user about their suspension and the reason
 *   - Revoke active sessions
 *   - Log the admin action for audit
 */
public class SUSPENDEDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(SUSPENDEDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public SUSPENDEDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        String reason = (String) user.getTransientMap().getOrDefault("reason", user.getSuspendReason());

        log.info("User {} suspended. Reason: {}", user.getId(), reason);

        eventPublisher.publishUserSuspended(
                new UserDomainEvents.UserSuspended(user.getId(), reason, "ADMIN", Instant.now())
        );
    }
}
