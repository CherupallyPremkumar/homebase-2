package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * SUSPENDED -> ACTIVE.
 *
 * Admin reinstates a suspended user. Clears the suspend reason
 * and resets security counters. The ACTIVE post-save hook publishes
 * a UserReinstated event.
 */
public class ReinstateUserAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        // Clear suspension data
        user.setSuspendReason(null);
        user.resetLoginAttempts();
        user.setLockReason(null);

        user.getTransientMap().put("event", "USER_REINSTATED");
    }
}
