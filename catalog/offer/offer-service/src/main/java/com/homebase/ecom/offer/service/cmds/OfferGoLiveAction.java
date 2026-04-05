package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * System transitions an approved offer to LIVE. Sets start date if not set.
 */
public class OfferGoLiveAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
    @Override
    public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (offer.getStartDate() == null) {
            offer.setStartDate(new java.util.Date());
        }
        offer.addActivity("goLive", "Offer is now live and visible to customers");
        offer.getTransientMap().put("previousPayload", payload);
    }
}
