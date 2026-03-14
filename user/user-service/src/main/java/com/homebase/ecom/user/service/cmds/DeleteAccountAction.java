package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ANY -> DELETED.
 *
 * Soft-deletes the user account. Anonymizes PII (email, names, phone)
 * by replacing with anonymized placeholders. Preserves the ID for
 * referential integrity with orders, payments, etc.
 *
 * The DELETED post-save hook publishes a UserDeleted event for
 * downstream systems to clean up (e.g., revoke Keycloak sessions).
 */
public class DeleteAccountAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        String deletedBy = (payload instanceof String s && !s.isBlank()) ? s : "SELF";

        // Preserve original email for the event (before anonymization)
        String originalEmail = user.getEmail();

        // Anonymize PII for GDPR/data protection compliance
        user.setEmail("deleted-" + user.getId() + "@anonymized.local");
        user.setFirstName("Deleted");
        user.setLastName("User");
        user.setPhone(null);
        user.setAvatarUrl(null);

        // Clear security state
        user.setLockReason(null);
        user.setSuspendReason(null);
        user.resetFailedLoginAttempts();

        // Signal to post-save hook
        user.getTransientMap().put("event", "ACCOUNT_DELETED");
        user.getTransientMap().put("deletedBy", deletedBy);
        user.getTransientMap().put("originalEmail", originalEmail);
    }
}
