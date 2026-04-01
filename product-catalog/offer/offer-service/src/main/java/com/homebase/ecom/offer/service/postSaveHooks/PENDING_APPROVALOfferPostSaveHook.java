package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for PENDING_APPROVAL state.
 * Notifies catalog admins that an offer is awaiting review.
 */
public class PENDING_APPROVALOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(PENDING_APPROVALOfferPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("Offer {} for product {} by seller {} is now PENDING_APPROVAL",
                offer.getId(), offer.getProductId(), offer.getSupplierId());
    }
}
