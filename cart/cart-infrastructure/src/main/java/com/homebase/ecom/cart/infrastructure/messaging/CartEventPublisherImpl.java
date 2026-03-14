package com.homebase.ecom.cart.infrastructure.messaging;

import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class CartEventPublisherImpl implements CartEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CartEventPublisherImpl.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CartEventPublisherImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Scheduling OrderCreatedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    @Override
    public void publishCartCheckoutCompleted(CartCheckoutCompletedEvent event) {
        log.info("Scheduling CartCheckoutCompletedEvent publish after commit for cart: {}", event.getCartId());
        sendAfterCommit(KafkaTopics.CART_EVENTS, event.getCartId(), event);
    }

    @Override
    public void publishCartCheckoutInitiated(CartCheckoutInitiatedEvent event) {
        log.info("Scheduling CartCheckoutInitiatedEvent publish after commit for cart: {}", event.getCartId());
        sendAfterCommit(KafkaTopics.CART_EVENTS, event.getCartId(), event);
    }

    @Override
    public void publishCartAbandoned(CartAbandonedEvent event) {
        log.info("Scheduling CartAbandonedEvent publish after commit for cart: {}", event.getCartId());
        sendAfterCommit(KafkaTopics.CART_EVENTS, event.getCartId(), event);
    }

    @Override
    public void publishCartCreated(CartCreatedEvent event) {
        log.info("Scheduling CartCreatedEvent publish after commit for cart: {}", event.getCartId());
        sendAfterCommit(KafkaTopics.CART_EVENTS, event.getCartId(), event);
    }

    private void sendAfterCommit(String topic, String key, Object payload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaTemplate.send(topic, key, payload);
                    log.info("Event published to Kafka topic {}: {}", topic, key);
                }
            });
        } else {
            kafkaTemplate.send(topic, key, payload);
            log.info("Event published to Kafka topic {} (no transaction): {}", topic, key);
        }
    }
}
