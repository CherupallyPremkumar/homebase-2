package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> ACTIVE (self-transition).
 *
 * Updates mutable profile fields (firstName, lastName, phone, avatarUrl).
 * Only non-null incoming fields are applied (partial update semantics).
 * Email and keycloakId are immutable and cannot be changed through this action.
 */
public class UpdateProfileAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (payload instanceof User incoming) {
            // Only update non-null fields (partial update)
            if (incoming.getFirstName() != null && !incoming.getFirstName().isBlank()) {
                user.setFirstName(incoming.getFirstName().trim());
            }
            if (incoming.getLastName() != null && !incoming.getLastName().isBlank()) {
                user.setLastName(incoming.getLastName().trim());
            }
            if (incoming.getPhone() != null) {
                user.setPhone(incoming.getPhone().trim());
            }
            if (incoming.getAvatarUrl() != null) {
                user.setAvatarUrl(incoming.getAvatarUrl().trim());
            }
        }

        user.getTransientMap().put("event", "PROFILE_UPDATED");
    }
}
