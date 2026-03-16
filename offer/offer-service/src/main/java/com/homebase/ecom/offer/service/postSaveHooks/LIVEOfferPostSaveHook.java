package com.homebase.ecom.offer.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.PricingPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OfferLiveEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Item 13: Post save hook for LIVE state.
 * Updates catalog pricing via PricingPort and publishes OFFER_LIVE event to offer.events.
 */
public class LIVEOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(LIVEOfferPostSaveHook.class);

    @Autowired(required = false)
    private PricingPort pricingPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        // Update catalog pricing
        if (pricingPort != null) {
            pricingPort.updateOfferPricing(offer.getProductId(), offer.getId(), offer.getOfferPrice());
        }

        // Publish OFFER_LIVE event
        if (chenilePub != null) {
            OfferLiveEvent event = new OfferLiveEvent(
                    offer.getId(), offer.getProductId(), offer.getSupplierId(),
                    offer.getOfferType() != null ? offer.getOfferType().name() : null,
                    offer.getOriginalPrice(), offer.getOfferPrice(), offer.getDiscountPercent());
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.OFFER_EVENTS, body,
                        Map.of("key", offer.getProductId(), "eventType", OfferLiveEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize OfferLiveEvent for offer={}", offer.getId(), e);
                return;
            }
        }

        log.info("OFFER_LIVE: Offer {} for product {} is now live. Price: {} -> {}",
                offer.getId(), offer.getProductId(), offer.getOriginalPrice(), offer.getOfferPrice());
    }
}
