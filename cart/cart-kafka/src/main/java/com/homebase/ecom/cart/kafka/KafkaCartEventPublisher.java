package com.homebase.ecom.cart.kafka;

import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Kafka event publisher for cart events — dumb pipe.
 * Serializes the domain event to JSON and publishes to "cart.events" topic.
 * Publish-after-commit for transaction safety.
 * No business logic, no event type mapping.
 */
public class KafkaCartEventPublisher implements CartEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaCartEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaCartEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(CartEvent event) {
        Runnable publishAction = () -> {
            try {
                String json = objectMapper.writeValueAsString(event);
                chenilePub.asyncPublish(KafkaTopics.CART_EVENTS, json,
                        Map.of("key", event.getCartId(), "eventType", event.getEventType()));
                log.info("Published {} event for cartId={}", event.getEventType(), event.getCartId());
            } catch (JacksonException e) {
                log.error("Failed to serialize {} for cartId={}", event.getEventType(), event.getCartId(), e);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
        } else {
            publishAction.run();
        }
    }
}
