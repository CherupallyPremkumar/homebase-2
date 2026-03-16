package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * EMAIL_VERIFIED -> ACTIVE.
 *
 * System activates the user after email verification is confirmed.
 * Resets security counters and signals the ACTIVE post-save hook
 * to publish UserVerified event and enable features.
 */
public class ActivateAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        user.resetLoginAttempts();
        user.setLockReason(null);
        user.setSuspendReason(null);

        // Default role if not set
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("CUSTOMER");
        }

        user.getTransientMap().put("event", "USER_ACTIVATED");
    }
}
