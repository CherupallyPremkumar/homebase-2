package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.dto.ChangePasswordPayload;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> ACTIVE (self-transition).
 *
 * Validates that old and new passwords are provided, then stores them in the
 * transient map for the infrastructure layer to handle the actual Keycloak
 * password change. The domain layer does not store password hashes since
 * authentication is delegated to Keycloak.
 */
public class ChangePasswordAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (payload instanceof ChangePasswordPayload changePasswordPayload) {
            // Validate required fields
            if (changePasswordPayload.getOldPassword() == null || changePasswordPayload.getOldPassword().isBlank()) {
                throw new IllegalArgumentException("Old password is required");
            }
            if (changePasswordPayload.getNewPassword() == null || changePasswordPayload.getNewPassword().isBlank()) {
                throw new IllegalArgumentException("New password is required");
            }
            if (changePasswordPayload.getNewPassword().length() < 8) {
                throw new IllegalArgumentException("New password must be at least 8 characters");
            }
            if (changePasswordPayload.getOldPassword().equals(changePasswordPayload.getNewPassword())) {
                throw new IllegalArgumentException("New password must be different from old password");
            }

            // Store in transient map for post-save hook / infrastructure to process
            user.getTransientMap().put("event", "PASSWORD_CHANGED");
            user.getTransientMap().put("oldPassword", changePasswordPayload.getOldPassword());
            user.getTransientMap().put("newPassword", changePasswordPayload.getNewPassword());
        } else {
            throw new IllegalArgumentException("ChangePasswordPayload is required");
        }
    }
}
