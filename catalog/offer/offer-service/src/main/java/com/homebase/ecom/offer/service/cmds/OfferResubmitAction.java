package com.homebase.ecom.offer.service.cmds;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.service.validator.OfferPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Seller resubmits a rejected offer for review. Re-validates policies.
 */
public class OfferResubmitAction extends AbstractSTMTransitionAction<Offer, MinimalPayload> {

    @Autowired
    private OfferPolicyValidator policyValidator;

    @Override
    public void transitionTo(Offer offer, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        policyValidator.validateForSubmission(offer);
        offer.addActivity("resubmit", "Offer resubmitted for review");
        offer.getTransientMap().put("previousPayload", payload);
    }
}
