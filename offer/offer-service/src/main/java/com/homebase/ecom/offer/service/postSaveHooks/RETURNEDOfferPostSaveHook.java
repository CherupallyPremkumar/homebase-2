package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the RETURNED state.
 * Supplier has withdrawn/returned the offer. Catalog should delist.
 */
public class RETURNEDOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(RETURNEDOfferPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("OfferReturnedEvent: Offer {} by supplier {} was returned/withdrawn.",
                offer.getId(), offer.getSupplierId());

        map.put("eventType", "OfferReturnedEvent");
        map.put("offerId", offer.getId());
        map.put("variantId", offer.getVariantId());
        map.put("supplierId", offer.getSupplierId());
    }
}
