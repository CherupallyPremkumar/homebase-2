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
 * Supplier resubmits their application after it was rejected.
 */
public class ResubmitAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResubmitAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        saga.setErrorMessage(null);
        saga.setRetryCount(saga.getRetryCount() + 1);
        log.info("Supplier {} resubmitted onboarding application (attempt #{})",
                saga.getSupplierName(), saga.getRetryCount());
    }
}
