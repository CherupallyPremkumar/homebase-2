package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Catalog admin rejects an offer with a required reason.
 */
public class OfferRejectAction extends AbstractSTMTransitionAction<Offer, RejectOfferPayload> {
    @Override
    public void transitionTo(Offer offer, RejectOfferPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = null;
        if (payload != null) {
            reason = payload.getReason() != null ? payload.getReason() : payload.getComment();
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting an offer");
        }
        offer.addActivity("reject", "Offer rejected. Reason: " + reason);
        offer.getTransientMap().put("previousPayload", payload);
    }
}
