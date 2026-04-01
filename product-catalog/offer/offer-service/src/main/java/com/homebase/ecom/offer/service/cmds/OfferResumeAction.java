package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Admin resumes a suspended offer back to LIVE.
 */
public class OfferResumeAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
    @Override
    public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        offer.addActivity("resume", "Offer resumed from suspended state");
        offer.getTransientMap().put("previousPayload", payload);
    }
}
