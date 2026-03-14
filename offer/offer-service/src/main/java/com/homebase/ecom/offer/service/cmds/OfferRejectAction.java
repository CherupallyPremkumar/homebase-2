package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Admin rejects a supplier offer with a required reason.
 */
public class OfferRejectAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
    @Override
    public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String comment = (payload != null && payload.getComment() != null && !payload.getComment().trim().isEmpty())
                ? payload.getComment() : null;
        if (comment == null) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting an offer");
        }
        offer.setStatus(OfferStatus.REJECTED);
        offer.addActivity("reject", "Offer rejected by Hub Manager. Reason: " + comment);
        offer.getTransientMap().put("previousPayload", payload);
    }
}
