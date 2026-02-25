package com.homebase.ecom.inventory.service.listener;

import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.shared.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class InventoryKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryKafkaListener.class);

    private final InventoryService inventoryService;
    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public InventoryKafkaListener(InventoryService inventoryService,
            org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "inventory-group")
    @Transactional
    public void onOrderEvent(OrderCreatedEvent event) {
        if (!OrderCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Inventory: received OrderCreatedEvent for order: {}", event.getOrderId());
        try {
            java.util.Map<String, Integer> items = new java.util.HashMap<>();
            event.getItems().forEach(item -> items.put(item.getProductId(), item.getQuantity()));

            inventoryService.reserveForOrder(event.getOrderId(), items);

            log.info("Stock reserved for order: {}", event.getOrderId());
            sendAfterCommit(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                    new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_RESERVED));
        } catch (Exception e) {
            log.error("Failed to reserve stock for order: {}, error: {}",
                    event.getOrderId(), e.getMessage());
            kafkaTemplate.send(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                    new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_FAILED));
        }
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_EVENTS, groupId = "inventory-group")
    public void onPaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        switch (envelope.getEventType()) {
            case PaymentSucceededEvent.EVENT_TYPE: {
                PaymentSucceededEvent event = objectMapper.convertValue(envelope.getPayload(), PaymentSucceededEvent.class);
                log.info("Inventory: received PaymentSucceededEvent for order: {}", event.getOrderId());
                inventoryService.commit(event.getOrderId());
                break;
            }
            case PaymentSessionExpiredEvent.EVENT_TYPE: {
                PaymentSessionExpiredEvent event = objectMapper.convertValue(envelope.getPayload(), PaymentSessionExpiredEvent.class);
                log.info("Inventory: received PaymentSessionExpiredEvent for order: {}", event.getOrderId());
                inventoryService.release(event.getOrderId());
                break;
            }
            case PaymentFailedEvent.EVENT_TYPE: {
                PaymentFailedEvent event = objectMapper.convertValue(envelope.getPayload(), PaymentFailedEvent.class);
                log.info("Inventory: received PaymentFailedEvent for order: {}", event.getOrderId());
                inventoryService.release(event.getOrderId());
                break;
            }
            default:
                // ignore
        }
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_EVENTS, groupId = "inventory-group")
    @Transactional
    public void onInventoryEvent(InventoryEvent event) {
        if (InventoryEvent.RELEASE_STOCK.equals(event.getEventType())) {
            log.info("Inventory: received release-stock command for order: {}", event.getOrderId());
            inventoryService.release(event.getOrderId());
            sendAfterCommit(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                    new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_RELEASED));
        }
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
