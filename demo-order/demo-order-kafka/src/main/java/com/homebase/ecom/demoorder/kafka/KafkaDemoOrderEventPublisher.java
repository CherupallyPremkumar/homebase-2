package com.homebase.ecom.demoorder.kafka;

import com.homebase.ecom.demoorder.event.DemoOrderEvent;
import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Kafka event publisher for demo order events -- dumb pipe.
 * Serializes the domain event to JSON and publishes to "demo-order.events" topic.
 * Publish-after-commit for transaction safety.
 */
public class KafkaDemoOrderEventPublisher implements DemoOrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaDemoOrderEventPublisher.class);
    private static final String TOPIC = "demo-order.events";

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaDemoOrderEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DemoOrderEvent event) {
        Runnable publishAction = () -> {
            try {
                String json = objectMapper.writeValueAsString(event);
                chenilePub.asyncPublish(TOPIC, json,
                        Map.of("key", event.getOrderId(), "eventType", event.getEventType()));
                log.info("Published {} event for orderId={}", event.getEventType(), event.getOrderId());
            } catch (JacksonException e) {
                log.error("Failed to serialize {} for orderId={}", event.getEventType(), event.getOrderId(), e);
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
