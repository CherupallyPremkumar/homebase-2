package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the REJECTED state.
 * Notifies the supplier that their offer was rejected.
 */
public class REJECTEDOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(REJECTEDOfferPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("OfferRejectedEvent: Offer {} by supplier {} was rejected.",
                offer.getId(), offer.getSupplierId());

        map.put("eventType", "OfferRejectedEvent");
        map.put("offerId", offer.getId());
        map.put("supplierId", offer.getSupplierId());
    }
}
