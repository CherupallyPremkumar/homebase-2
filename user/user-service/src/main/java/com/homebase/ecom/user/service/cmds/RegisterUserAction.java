package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * Initial create action -- fired when a new User is created (POST /user).
 *
 * Sets identity fields from the incoming payload. User starts in REGISTERED
 * state and must verify email to proceed.
 *
 * The REGISTERED post-save hook publishes USER_REGISTERED event to user.events
 * and triggers verification email via IdentityProviderPort.
 */
public class RegisterUserAction implements STMTransitionAction<User> {

    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (payload instanceof User incoming) {
            // Copy identity fields from the incoming payload
            if (incoming.getKeycloakId() != null) {
                user.setKeycloakId(incoming.getKeycloakId());
            }
            if (incoming.getEmail() != null) {
                user.setEmail(incoming.getEmail());
            }
            if (incoming.getFirstName() != null) {
                user.setFirstName(incoming.getFirstName());
            }
            if (incoming.getLastName() != null) {
                user.setLastName(incoming.getLastName());
            }
            if (incoming.getPhone() != null) {
                user.setPhone(incoming.getPhone());
            }
            // Set role -- default to CUSTOMER if not specified
            if (incoming.getRole() != null && !incoming.getRole().isBlank()) {
                user.setRole(incoming.getRole());
            } else {
                user.setRole("CUSTOMER");
            }
        }

        // Initialize security state
        user.resetLoginAttempts();

        // Signal to REGISTERED post-save hook
        user.getTransientMap().put("event", "REGISTERED");
    }
}
