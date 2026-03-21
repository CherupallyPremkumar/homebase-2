package com.homebase.ecom.checkout.kafka;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutEvent;
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
 * Kafka event publisher for checkout events — dumb pipe.
 * Serializes the domain event to JSON and publishes to "checkout.events" topic.
 * Publish-after-commit for transaction safety.
 * No business logic, no event type mapping.
 */
public class KafkaCheckoutEventPublisher implements CheckoutEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaCheckoutEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaCheckoutEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(CheckoutEvent event) {
        Runnable publishAction = () -> {
            try {
                String json = objectMapper.writeValueAsString(event);
                chenilePub.asyncPublish(KafkaTopics.CHECKOUT_EVENTS, json,
                        Map.of("key", event.getCheckoutId(), "eventType", event.getEventType()));
                log.info("Published {} event for checkoutId={}", event.getEventType(), event.getCheckoutId());
            } catch (JacksonException e) {
                log.error("Failed to serialize {} for checkoutId={}", event.getEventType(), event.getCheckoutId(), e);
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
