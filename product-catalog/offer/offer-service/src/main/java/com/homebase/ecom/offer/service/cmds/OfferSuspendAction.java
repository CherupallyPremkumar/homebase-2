package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Admin suspends a live offer (e.g., out of stock, compliance issue).
 */
public class OfferSuspendAction extends AbstractSTMTransitionAction<Offer, SuspendOfferPayload> {
    @Override
    public void transitionTo(Offer offer, SuspendOfferPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = null;
        if (payload != null) {
            reason = payload.getReason() != null ? payload.getReason() : payload.getComment();
        }
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Offer suspended by admin";
        }
        offer.addActivity("suspend", "Offer suspended. Reason: " + reason);
        offer.getTransientMap().put("suspendReason", reason);
        offer.getTransientMap().put("previousPayload", payload);
    }
}
