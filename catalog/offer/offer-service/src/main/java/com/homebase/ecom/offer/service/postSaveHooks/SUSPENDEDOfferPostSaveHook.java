package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.NotificationPort;
import com.homebase.ecom.offer.domain.port.OfferEventPublisherPort;
import com.homebase.ecom.offer.domain.port.PricingPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for SUSPENDED state.
 * Notifies seller, reverts pricing, and publishes OFFER_SUSPENDED event.
 */
public class SUSPENDEDOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(SUSPENDEDOfferPostSaveHook.class);

    private final NotificationPort notificationPort;
    private final PricingPort pricingPort;
    private final OfferEventPublisherPort eventPublisher;

    public SUSPENDEDOfferPostSaveHook(NotificationPort notificationPort, PricingPort pricingPort,
                                       OfferEventPublisherPort eventPublisher) {
        this.notificationPort = notificationPort;
        this.pricingPort = pricingPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        String reason = (String) map.get("suspendReason");
        if (reason == null) reason = "Offer suspended";

        // Notify seller
        if (notificationPort != null) {
            notificationPort.notifyOfferSuspended(
                    offer.getSupplierId(), offer.getId(), offer.getProductId(), reason);
        }

        // Revert pricing since offer is no longer live
        if (pricingPort != null) {
            pricingPort.revertOfferPricing(offer.getProductId(), offer.getId());
        }

        // Publish OFFER_SUSPENDED event
        eventPublisher.publishOfferSuspended(offer, reason);

        log.info("OFFER_SUSPENDED: Offer {} for product {} suspended. Reason: {}",
                offer.getId(), offer.getProductId(), reason);
    }
}
