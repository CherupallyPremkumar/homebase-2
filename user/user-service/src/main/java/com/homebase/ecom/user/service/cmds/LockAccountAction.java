package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> LOCKED.
 *
 * Triggered when consecutive failed logins reach the threshold (3).
 * Sets the lock reason on the user and signals the LOCKED post-save hook
 * to publish an AccountLocked event and notify the user.
 */
public class LockAccountAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        String reason = "3 consecutive failed login attempts";
        if (payload instanceof String s && !s.isBlank()) {
            reason = s;
        }

        user.setLockReason(reason);

        // Signal to post-save hook
        user.getTransientMap().put("event", "ACCOUNT_LOCKED");
        user.getTransientMap().put("reason", reason);
        user.getTransientMap().put("failedAttempts", String.valueOf(user.getFailedLoginAttempts()));
    }
}
