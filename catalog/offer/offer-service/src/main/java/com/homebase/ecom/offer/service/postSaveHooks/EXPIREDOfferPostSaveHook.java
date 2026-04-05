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
 * Post save hook for EXPIRED state.
 * Reverts catalog pricing via PricingPort and publishes OFFER_EXPIRED event.
 */
public class EXPIREDOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(EXPIREDOfferPostSaveHook.class);

    private final PricingPort pricingPort;
    private final OfferEventPublisherPort eventPublisher;

    public EXPIREDOfferPostSaveHook(PricingPort pricingPort, OfferEventPublisherPort eventPublisher) {
        this.pricingPort = pricingPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        // Revert catalog pricing
        if (pricingPort != null) {
            pricingPort.revertOfferPricing(offer.getProductId(), offer.getId());
        }

        // Publish OFFER_EXPIRED event
        eventPublisher.publishOfferExpired(offer);

        log.info("OFFER_EXPIRED: Offer {} for product {} has expired. Pricing reverted.",
                offer.getId(), offer.getProductId());
    }
}
