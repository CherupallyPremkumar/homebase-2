package com.homebase.ecom.order.infrastructure.integration;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import com.homebase.ecom.shared.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kafka-based infrastructure adapter for {@link OrderEventPublisherPort}.
 * Handles event construction, serialization, and transactional publish-after-commit.
 */
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

    @SuppressWarnings("rawtypes")
    private final KafkaTemplate kafkaTemplate;

    @SuppressWarnings("rawtypes")
    public KafkaOrderEventPublisher(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setCustomerId(order.getCustomerId());
        if (order.getTotalAmount() != null) {
            event.setTotalAmount(order.getTotalAmount());
        }
        event.setTimestamp(LocalDateTime.now());

        if (order.getItems() != null) {
            event.setItems(order.getItems().stream()
                    .map(item -> new OrderCreatedEvent.OrderItemPayload(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList()));
        }

        log.info("Publishing ORDER_CREATED event for order: {}, items: {}",
                order.getId(), order.getItems() != null ? order.getItems().size() : 0);
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), event);
    }

    @Override
    public void publishOrderPaid(Order order) {
        log.info("Publishing ORDER_PAID event for order: {}", order.getId());
        EventEnvelope envelope = EventEnvelope.of("ORDER_PAID", 1,
                Map.of("orderId", order.getId(),
                        "customerId", order.getCustomerId() != null ? order.getCustomerId() : "",
                        "timestamp", LocalDateTime.now().toString()));
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), envelope);
    }

    @Override
    public void publishOrderCancelled(Order order) {
        List<OrderCancelledEvent.OrderItemPayload> items = new ArrayList<>();
        if (order.getItems() != null) {
            items = order.getItems().stream()
                    .map(item -> new OrderCancelledEvent.OrderItemPayload(
                            item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());
        }

        OrderCancelledEvent event = new OrderCancelledEvent(
                order.getId(), order.getCustomerId(), items, LocalDateTime.now());

        log.info("Publishing ORDER_CANCELLED event for order: {}, reason: {}",
                order.getId(), order.getCancelReason());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), event);
    }

    @Override
    public void publishOrderShipped(Order order, String carrier, String trackingNumber) {
        OrderShippedEvent event = new OrderShippedEvent(
                order.getId(), carrier, trackingNumber, null, LocalDateTime.now());

        log.info("Publishing ORDER_SHIPPED event for order: {}, carrier: {}", order.getId(), carrier);
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), event);
    }

    @Override
    public void publishOrderDelivered(Order order) {
        OrderDeliveredEvent event = new OrderDeliveredEvent(
                order.getId(), order.getCustomerId(), LocalDateTime.now());

        log.info("Publishing ORDER_DELIVERED event for order: {}", order.getId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), event);
    }

    @Override
    public void publishOrderCompleted(Order order) {
        List<OrderCompletedEvent.CompletedItem> completedItems = new ArrayList<>();
        if (order.getItems() != null) {
            completedItems = order.getItems().stream()
                    .map(item -> new OrderCompletedEvent.CompletedItem(
                            item.getProductId(), null, item.getQuantity(),
                            item.getTotalPrice()))
                    .collect(Collectors.toList());
        }

        OrderCompletedEvent event = new OrderCompletedEvent(
                order.getId(), order.getCustomerId(), LocalDateTime.now(), completedItems);

        log.info("Publishing ORDER_COMPLETED event for order: {}", order.getId());
        sendAfterCommit(KafkaTopics.ORDER_EVENTS, order.getId(), event);
    }

    @SuppressWarnings("unchecked")
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
