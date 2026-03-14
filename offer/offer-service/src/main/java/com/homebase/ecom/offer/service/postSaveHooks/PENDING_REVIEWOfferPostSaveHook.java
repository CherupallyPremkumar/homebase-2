package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Contains customized post Save Hook for the PENDING_REVIEW state.
 */
public class PENDING_REVIEWOfferPostSaveHook implements PostSaveHook<Offer> {
    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
    }
}
