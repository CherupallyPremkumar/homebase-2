package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Admin approves a supplier offer. Sets status to ACTIVE and establishes the trial period.
 */
public class OfferApproveAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {
    @Override
    public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        offer.setStatus(OfferStatus.ACTIVE);
        // Set trial end date to 15 days from now if not already set
        if (offer.getTrialEndDate() == null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DAY_OF_MONTH, 15);
            offer.setTrialEndDate(cal.getTime());
        }
        offer.addActivity("approve", "Offer approved by Hub Manager. Trial period started.");
        offer.getTransientMap().put("previousPayload", payload);
    }
}
