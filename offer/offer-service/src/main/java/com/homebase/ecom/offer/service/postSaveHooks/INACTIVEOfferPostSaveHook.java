package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the INACTIVE state.
 * Publishes OfferDeactivatedEvent so Catalog BC can hide the product listing.
 */
public class INACTIVEOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(INACTIVEOfferPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("OfferDeactivatedEvent: Offer {} for variant {} deactivated from state {}.",
                offer.getId(), offer.getVariantId(), startState.getStateId());

        map.put("eventType", "OfferDeactivatedEvent");
        map.put("offerId", offer.getId());
        map.put("variantId", offer.getVariantId());
        map.put("supplierId", offer.getSupplierId());
    }
}
