package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Lifecycle transition actions for Offer state machine.
 */
public class OfferLifecycleActions {

    /**
     * Reactivates a deactivated offer back to ACTIVE.
     */
    public static class ActivateAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
        @Override
        public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
                State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            offer.setStatus(OfferStatus.ACTIVE);
            offer.addActivity("activate", "Offer reactivated from " + startState.getStateId());
            offer.getTransientMap().put("previousPayload", payload);
        }
    }

    /**
     * Temporarily deactivates an active offer.
     */
    public static class DeactivateAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
        @Override
        public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
                State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            offer.setStatus(OfferStatus.INACTIVE);
            offer.addActivity("deactivate", "Offer temporarily deactivated");
            offer.getTransientMap().put("previousPayload", payload);
        }
    }

    /**
     * Supplier withdraws/returns an offer (e.g., trial failure or voluntary withdrawal).
     */
    public static class ReturnAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
        @Override
        public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
                State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            offer.setStatus(OfferStatus.RETURNED);
            String comment = (payload != null && payload.getComment() != null) ? payload.getComment() : "Offer withdrawn by supplier";
            offer.addActivity("returnOffer", comment);
            offer.getTransientMap().put("previousPayload", payload);
        }
    }

    /**
     * Supplier resubmits a rejected offer for review.
     */
    public static class ResubmitAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
        @Override
        public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
                State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            offer.setStatus(OfferStatus.PENDING_REVIEW);
            offer.addActivity("resubmit", "Offer resubmitted for review");
            offer.getTransientMap().put("previousPayload", payload);
        }
    }
}
