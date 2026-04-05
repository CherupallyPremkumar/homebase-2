package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post save hook for DRAFT state. No-op for initial creation.
 */
public class DRAFTOfferPostSaveHook implements PostSaveHook<Offer> {
    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        // No action needed when offer enters DRAFT
    }
}
