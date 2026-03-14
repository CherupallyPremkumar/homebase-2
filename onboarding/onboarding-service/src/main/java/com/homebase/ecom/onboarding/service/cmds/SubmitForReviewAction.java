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
 * Submits the onboarding application for admin review after documents are verified.
 * Triggers an admin notification about the pending review.
 */
public class SubmitForReviewAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SubmitForReviewAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        log.info("Submitting onboarding application for review: name={}, email={}",
                saga.getSupplierName(), saga.getEmail());

        // Trigger admin notification about pending review
        saga.getTransientMap().put("adminNotificationRequired", true);
        saga.getTransientMap().put("reviewSubmittedAt", new java.util.Date());
    }
}
