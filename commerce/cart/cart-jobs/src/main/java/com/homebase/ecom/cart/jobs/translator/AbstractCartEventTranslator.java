package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Base class for event translators.
 * Translates external domain events into cart STM events published to cart.events.
 *
 * Pattern: Fan-out — one external event may affect N carts.
 * Each affected cart gets its own event on cart.events topic.
 *
 * The cart-service @ChenilePubSub(subscribeTopic="cart.events") controller picks up
 * these events and routes them through the STM via processById.
 */
public abstract class AbstractCartEventTranslator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final ChenilePub chenilePub;

    protected AbstractCartEventTranslator(ChenilePub chenilePub) {
        this.chenilePub = chenilePub;
    }

    /**
     * Publishes a cart STM command to cart.commands topic.
     * The Chenile @ChenilePubSub handler in cart-service picks this up
     * and calls processById(cartId, eventId, payload).
     */
    protected void publishCartEvent(String cartId, String eventId, String payload) {
        try {
            chenilePub.asyncPublish(KafkaTopics.CART_EVENTS, payload,
                    Map.of("key", cartId,
                           "eventType", eventId,
                           "cartId", cartId,
                           "source", "cart-jobs"));
            log.debug("Published {} event for cartId={}", eventId, cartId);
        } catch (Exception e) {
            log.error("Failed to publish {} event for cartId={}", eventId, cartId, e);
        }
    }

    /**
     * Fan-out: publishes the same event for each affected cart.
     */
    protected void fanOut(List<String> cartIds, String eventId, String payload) {
        if (cartIds == null || cartIds.isEmpty()) {
            log.debug("No carts affected for event {}", eventId);
            return;
        }
        log.info("Fan-out: publishing {} event to {} carts", eventId, cartIds.size());
        for (String cartId : cartIds) {
            publishCartEvent(cartId, eventId, payload);
        }
    }
}
