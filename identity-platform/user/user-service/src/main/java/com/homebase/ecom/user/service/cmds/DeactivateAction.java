package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ANY -> DEACTIVATED.
 *
 * Soft-deactivates the user account. Anonymizes PII (email, names, phone)
 * for GDPR compliance. Preserves ID for referential integrity.
 * The DEACTIVATED post-save hook publishes USER_DEACTIVATED event.
 */
public class DeactivateAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        String deactivatedBy = "SELF";
        if (payload instanceof String s && !s.isBlank()) {
            deactivatedBy = s;
        }

        // Preserve original email for the event (before anonymization)
        String originalEmail = user.getEmail();

        // Anonymize PII for GDPR/data protection compliance
        user.setEmail("deactivated-" + user.getId() + "@anonymized.local");
        user.setFirstName("Deactivated");
        user.setLastName("User");
        user.setPhone(null);

        // Clear security state
        user.setLockReason(null);
        user.setSuspendReason(null);
        user.resetLoginAttempts();

        // Signal to post-save hook
        user.getTransientMap().put("event", "USER_DEACTIVATED");
        user.getTransientMap().put("deactivatedBy", deactivatedBy);
        user.getTransientMap().put("originalEmail", originalEmail);
    }
}
