package com.homebase.ecom.offer.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.NotificationPort;
import com.homebase.ecom.offer.domain.port.PricingPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OfferSuspendedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Item 13: Post save hook for SUSPENDED state.
 * Notifies seller, reverts pricing, and publishes OFFER_SUSPENDED event.
 */
public class SUSPENDEDOfferPostSaveHook implements PostSaveHook<Offer> {

    private static final Logger log = LoggerFactory.getLogger(SUSPENDEDOfferPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private PricingPort pricingPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

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
        if (chenilePub != null) {
            OfferSuspendedEvent event = new OfferSuspendedEvent(
                    offer.getId(), offer.getProductId(), offer.getSupplierId(), reason);
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.OFFER_EVENTS, body,
                        Map.of("key", offer.getProductId(), "eventType", OfferSuspendedEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize OfferSuspendedEvent for offer={}", offer.getId(), e);
                return;
            }
        }

        log.info("OFFER_SUSPENDED: Offer {} for product {} suspended. Reason: {}",
                offer.getId(), offer.getProductId(), reason);
    }
}
