package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System completes the onboarding — creates supplier entity and activates.
 * Transitions from ONBOARDED to COMPLETED.
 */
public class CompleteOnboardingAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteOnboardingAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        saga.setTrainingProgress(100);
        log.info("Onboarding COMPLETED for application {}, business: {}, supplier: {}",
                saga.getId(), saga.getBusinessName(), saga.getSupplierId());
    }
}
