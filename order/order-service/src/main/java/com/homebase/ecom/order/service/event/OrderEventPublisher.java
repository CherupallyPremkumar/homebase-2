package com.homebase.ecom.order.service.event;

import com.homebase.ecom.shared.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

/**
 * Publishes order lifecycle events to order.events Kafka topic.
 * Events: ORDER_CREATED, ORDER_PAID, ORDER_CANCELLED, ORDER_SHIPPED,
 *         ORDER_DELIVERED, ORDER_COMPLETED
 */
@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Scheduling ORDER_CREATED publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderPaid(String orderId, String customerId) {
        log.info("Scheduling ORDER_PAID publish for order: {}", orderId);
        EventEnvelope envelope = EventEnvelope.of("ORDER_PAID", 1,
                java.util.Map.of("orderId", orderId, "customerId", customerId != null ? customerId : "",
                        "timestamp", LocalDateTime.now().toString()));
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, orderId, envelope);
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        log.info("Scheduling ORDER_CANCELLED publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderShipped(OrderShippedEvent event) {
        log.info("Scheduling ORDER_SHIPPED publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderDelivered(OrderDeliveredEvent event) {
        log.info("Scheduling ORDER_DELIVERED publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderCompleted(OrderCompletedEvent event) {
        log.info("Scheduling ORDER_COMPLETED publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishSettlementRequested(SettlementRequestedEvent event) {
        log.info("Scheduling SettlementRequestedEvent publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.SETTLEMENT_EVENTS, event.getOrderId(), event);
    }

    public void publishSettlementAdjustment(SettlementAdjustmentEvent event) {
        log.info("Scheduling SettlementAdjustmentEvent publish for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.SETTLEMENT_EVENTS, event.getOrderId(), event);
    }

    private void sendAfterCommit(String topic, String key, Object payload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaTemplate.send(topic, key, payload);
                }
            });
        } else {
            kafkaTemplate.send(topic, key, payload);
        }
    }
}
