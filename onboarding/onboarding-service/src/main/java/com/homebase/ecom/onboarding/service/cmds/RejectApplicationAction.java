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
 * Admin rejects the application at the initial APPLICATION_SUBMITTED stage.
 */
public class RejectApplicationAction extends AbstractSTMTransitionAction<OnboardingSaga, RejectOnboardingPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectApplicationAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, RejectOnboardingPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String reason = payload.getReason();
        saga.setErrorMessage(reason != null ? reason : "Application rejected at submission");
        log.info("Onboarding application REJECTED at submission for supplier: {}, reason: {}",
                saga.getSupplierName(), saga.getErrorMessage());
    }
}
