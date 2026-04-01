package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * PENDING_VERIFICATION -> PENDING_VERIFICATION (self-transition).
 *
 * Triggers re-sending the verification email. The actual email dispatch
 * happens in the PENDING_VERIFICATION post-save hook via the event publisher.
 */
public class ResendVerificationAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalStateException("Cannot resend verification: no email address on file");
        }

        // Signal to post-save hook to re-send verification
        user.getTransientMap().put("event", "VERIFICATION_RESENT");
        user.getTransientMap().put("email", user.getEmail());
    }
}
