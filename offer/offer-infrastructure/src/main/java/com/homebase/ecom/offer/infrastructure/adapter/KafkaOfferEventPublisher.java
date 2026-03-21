package com.homebase.ecom.offer.infrastructure.adapter;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OfferExpiredEvent;
import com.homebase.ecom.shared.event.OfferLiveEvent;
import com.homebase.ecom.shared.event.OfferSuspendedEvent;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Kafka-based infrastructure adapter for {@link OfferEventPublisherPort}.
 * Uses ChenilePub for Kafka publishing with Chenile header propagation.
 * Handles event construction, serialization, and error handling.
 */
public class KafkaOfferEventPublisher implements OfferEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaOfferEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaOfferEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishOfferLive(Offer offer) {
        OfferLiveEvent event = new OfferLiveEvent(
                offer.getId(), offer.getProductId(), offer.getSupplierId(),
                offer.getOfferType() != null ? offer.getOfferType().name() : null,
                offer.getOriginalPrice(), offer.getOfferPrice(), offer.getDiscountPercent());
        publishEvent(event, offer.getProductId(), OfferLiveEvent.EVENT_TYPE,
                "OfferLiveEvent", offer.getId());
        log.info("Published OFFER_LIVE for offer={}, product={}, price: {} -> {}",
                offer.getId(), offer.getProductId(), offer.getOriginalPrice(), offer.getOfferPrice());
    }

    @Override
    public void publishOfferExpired(Offer offer) {
        OfferExpiredEvent event = new OfferExpiredEvent(
                offer.getId(), offer.getProductId(), offer.getSupplierId());
        publishEvent(event, offer.getProductId(), OfferExpiredEvent.EVENT_TYPE,
                "OfferExpiredEvent", offer.getId());
        log.info("Published OFFER_EXPIRED for offer={}, product={}",
                offer.getId(), offer.getProductId());
    }

    @Override
    public void publishOfferSuspended(Offer offer, String reason) {
        OfferSuspendedEvent event = new OfferSuspendedEvent(
                offer.getId(), offer.getProductId(), offer.getSupplierId(), reason);
        publishEvent(event, offer.getProductId(), OfferSuspendedEvent.EVENT_TYPE,
                "OfferSuspendedEvent", offer.getId());
        log.info("Published OFFER_SUSPENDED for offer={}, product={}, reason={}",
                offer.getId(), offer.getProductId(), reason);
    }

    private void publishEvent(Object event, String key, String eventType, String eventName, String offerId) {
        if (chenilePub == null) {
            log.debug("ChenilePub not available, skipping {} publish", eventName);
            return;
        }
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.OFFER_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for offer={}", eventName, offerId, e);
        }
    }
}
