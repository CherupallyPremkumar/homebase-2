package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the ACTIVE state.
 * Publishes OfferApprovedEvent when an offer transitions to ACTIVE.
 * Catalog BC and Inventory BC consume this event to list the product.
 */
public class ACTIVEOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(ACTIVEOfferPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("OfferApprovedEvent: Offer {} for variant {} by supplier {} is now ACTIVE. " +
                "Price: {}. Trial ends: {}",
                offer.getId(), offer.getVariantId(), offer.getSupplierId(),
                offer.getPrice(), offer.getTrialEndDate());

        map.put("eventType", "OfferApprovedEvent");
        map.put("offerId", offer.getId());
        map.put("variantId", offer.getVariantId());
        map.put("supplierId", offer.getSupplierId());
        map.put("previousState", startState.getStateId());
    }
}
