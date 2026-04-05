package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferEventPublisherPort;
import com.homebase.ecom.offer.domain.port.PricingPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for LIVE state.
 * Updates catalog pricing via PricingPort and publishes OFFER_LIVE event.
 */
public class LIVEOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(LIVEOfferPostSaveHook.class);

    private final PricingPort pricingPort;
    private final OfferEventPublisherPort eventPublisher;

    public LIVEOfferPostSaveHook(PricingPort pricingPort, OfferEventPublisherPort eventPublisher) {
        this.pricingPort = pricingPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        // Update catalog pricing
        if (pricingPort != null) {
            pricingPort.updateOfferPricing(offer.getProductId(), offer.getId(), offer.getOfferPrice());
        }

        // Publish OFFER_LIVE event
        eventPublisher.publishOfferLive(offer);

        log.info("OFFER_LIVE: Offer {} for product {} is now live. Price: {} -> {}",
                offer.getId(), offer.getProductId(), offer.getOriginalPrice(), offer.getOfferPrice());
    }
}
