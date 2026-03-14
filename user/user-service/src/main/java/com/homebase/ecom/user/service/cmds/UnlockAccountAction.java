package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * LOCKED -> ACTIVE.
 *
 * Admin or automatic timeout unlocks the account.
 * Resets failed login counter and clears the lock reason.
 */
public class UnlockAccountAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        // Reset security state
        user.resetFailedLoginAttempts();
        user.setLockReason(null);

        // Determine who unlocked
        String unlockedBy = "ADMIN";
        if (payload instanceof String s && !s.isBlank()) {
            unlockedBy = s;
        }

        user.getTransientMap().put("event", "ACCOUNT_UNLOCKED");
        user.getTransientMap().put("unlockedBy", unlockedBy);
    }
}
