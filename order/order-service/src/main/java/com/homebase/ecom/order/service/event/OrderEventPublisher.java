package com.homebase.ecom.order.service.event;

import com.homebase.ecom.shared.model.event.KafkaTopics;
import com.homebase.ecom.shared.model.event.OrderCancelledEvent;
import com.homebase.ecom.shared.model.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Scheduling OrderCreatedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        log.info("Scheduling OrderCancelledEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
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
