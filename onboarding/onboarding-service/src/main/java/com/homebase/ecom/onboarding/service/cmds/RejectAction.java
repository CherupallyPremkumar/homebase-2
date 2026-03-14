package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.RejectOnboardingPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin rejects the onboarding application during review.
 * Requires a rejection reason and notifies the applicant.
 */
public class RejectAction extends AbstractSTMTransitionAction<OnboardingSaga, RejectOnboardingPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, RejectOnboardingPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }

        saga.setErrorMessage(reason);

        // Flag for notification to applicant
        saga.getTransientMap().put("rejectionNotificationRequired", true);

        log.info("Onboarding application REJECTED for supplier: {}, reason: {}",
                saga.getSupplierName(), reason);
    }
}
