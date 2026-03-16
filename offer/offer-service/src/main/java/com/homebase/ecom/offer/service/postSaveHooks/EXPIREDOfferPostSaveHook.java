package com.homebase.ecom.offer.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.PricingPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OfferExpiredEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Item 13: Post save hook for EXPIRED state.
 * Reverts catalog pricing via PricingPort and publishes OFFER_EXPIRED event.
 */
public class EXPIREDOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(EXPIREDOfferPostSaveHook.class);

    @Autowired(required = false)
    private PricingPort pricingPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        // Revert catalog pricing
        if (pricingPort != null) {
            pricingPort.revertOfferPricing(offer.getProductId(), offer.getId());
        }

        // Publish OFFER_EXPIRED event
        if (chenilePub != null) {
            OfferExpiredEvent event = new OfferExpiredEvent(
                    offer.getId(), offer.getProductId(), offer.getSupplierId());
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.OFFER_EVENTS, body,
                        Map.of("key", offer.getProductId(), "eventType", OfferExpiredEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize OfferExpiredEvent for offer={}", offer.getId(), e);
                return;
            }
        }

        log.info("OFFER_EXPIRED: Offer {} for product {} has expired. Pricing reverted.",
                offer.getId(), offer.getProductId());
    }
}
