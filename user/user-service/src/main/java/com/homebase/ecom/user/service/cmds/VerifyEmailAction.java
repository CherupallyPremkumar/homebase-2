package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.dto.VerifyEmailUserPayload;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * PENDING_VERIFICATION -> ACTIVE.
 *
 * Email token verified via Keycloak webhook or user-submitted OTP.
 * Resets any failed login counter and marks user as verified in transient map
 * so the ACTIVE post-save hook can publish UserActivated event.
 */
public class VerifyEmailAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        // Validate that user has an email to verify
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalStateException("Cannot verify email: no email address on file");
        }

        // Reset failed login attempts on successful verification
        user.resetFailedLoginAttempts();

        // Clear any lock/suspend reason that may have been set
        user.setLockReason(null);
        user.setSuspendReason(null);

        // Signal to post-save hook
        user.getTransientMap().put("event", "EMAIL_VERIFIED");
        user.getTransientMap().put("email", user.getEmail());
    }
}
