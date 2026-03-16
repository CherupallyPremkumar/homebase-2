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
 * Admin rejects the onboarding application at any rejectable state.
 * Requires a rejection reason.
 */
public class RejectAction extends AbstractSTMTransitionAction<OnboardingSaga, RejectOnboardingPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, RejectOnboardingPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String reason = payload.getReason();
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }

        saga.setRejectionReason(reason);
        log.info("Onboarding application REJECTED for business: {}, reason: {}",
                saga.getBusinessName(), reason);
    }
}
