package com.homebase.ecom.order.service.event;

import com.homebase.ecom.shared.event.*;
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

    public void publishOrderItemCancellationRequested(
            OrderItemCancellationRequestedEvent event) {
        log.info("Scheduling OrderItemCancellationRequestedEvent for order: {}, item: {}", event.getOrderId(),
                event.getOrderItemId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderItemRefundRequested(
            OrderItemRefundRequestedEvent event) {
        log.info("Scheduling OrderItemRefundRequestedEvent for order: {}, item: {}", event.getOrderId(),
                event.getOrderItemId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderProcessingStarted(OrderProcessingStartedEvent event) {
        log.info("Scheduling OrderProcessingStartedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderItemsPicked(OrderItemsPickedEvent event) {
        log.info("Scheduling OrderItemsPickedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderShipped(OrderShippedEvent event) {
        log.info("Scheduling OrderShippedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderDelivered(OrderDeliveredEvent event) {
        log.info("Scheduling OrderDeliveredEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderCompleted(OrderCompletedEvent event) {
        log.info("Scheduling OrderCompletedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishReturnInitiated(ReturnInitiatedEvent event) {
        log.info("Scheduling ReturnInitiatedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishRefundInitiated(RefundInitiatedEvent event) {
        log.info("Scheduling RefundInitiatedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishOrderRefunded(OrderRefundedEvent event) {
        log.info("Scheduling OrderRefundedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, event.getOrderId(), event);
    }

    public void publishSettlementRequested(SettlementRequestedEvent event) {
        log.info("Scheduling SettlementRequestedEvent publish after commit for order: {}", event.getOrderId());
        sendAfterCommit(KafkaTopics.SETTLEMENT_EVENTS, event.getOrderId(), event);
    }

    public void publishSettlementAdjustment(SettlementAdjustmentEvent event) {
        log.info("Scheduling SettlementAdjustmentEvent publish after commit for order: {}", event.getOrderId());
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
