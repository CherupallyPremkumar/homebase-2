package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> SUSPENDED.
 *
 * Admin-initiated suspension. Requires a reason (fraud, policy violation, etc.).
 * The reason is stored on the user for audit and displayed to the user on login attempts.
 */
public class SuspendUserAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        String reason;
        if (payload instanceof String s && !s.isBlank()) {
            reason = s;
        } else {
            reason = "Suspended by admin";
        }

        user.setSuspendReason(reason);

        user.getTransientMap().put("event", "USER_SUSPENDED");
        user.getTransientMap().put("reason", reason);
    }
}
