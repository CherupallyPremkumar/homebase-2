package com.homebase.ecom.inventory.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemQueryAdapter;
import com.homebase.ecom.inventory.service.InventoryService;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * Chenile event handler for inventory cross-service events.
 * Registered via inventoryEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * Replaces manual @KafkaListener classes (InventoryKafkaListener, InventoryProductListener).
 */
/**
 * Bean name "inventoryEventService" — must match the service JSON id
 * so ChenileServiceInitializer can resolve the service reference.
 */
public class InventoryEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventHandler.class);

    private final InventoryService inventoryService;
    private final InventoryItemQueryAdapter inventoryItemQueryAdapter;
    private final StateEntityServiceImpl<InventoryItem> inventoryStateEntityService;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public InventoryEventHandler(
            InventoryService inventoryService,
            InventoryItemQueryAdapter inventoryItemQueryAdapter,
            @Qualifier("_inventoryStateEntityService_") StateEntityServiceImpl<InventoryItem> inventoryStateEntityService,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.inventoryItemQueryAdapter = inventoryItemQueryAdapter;
        this.inventoryStateEntityService = inventoryStateEntityService;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── order.events ──────────────────────────────────────────────────────

    @Transactional
    public void handleOrderEvent(OrderCreatedEvent event) {
        if (!OrderCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Inventory: received OrderCreatedEvent for order: {}", event.getOrderId());
        try {
            java.util.Map<String, Integer> items = new java.util.HashMap<>();
            event.getItems().forEach(item -> items.put(item.getProductId(), item.getQuantity()));

            inventoryService.reserveForOrder(event.getOrderId(), items);

            log.info("Stock reserved for order: {}", event.getOrderId());
            publishAfterCommit(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                    new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_RESERVED));
        } catch (IllegalStateException e) {
            log.warn("Idempotency: stock already reserved for order {} (possible replay). Skipping. Detail: {}",
                    event.getOrderId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to reserve stock for order: {}, error: {}",
                    event.getOrderId(), e.getMessage());
            publish(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                    new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_FAILED));
        }
    }

    // ── payment.events ────────────────────────────────────────────────────

    public void handlePaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        try {
            switch (envelope.getEventType()) {
                case PaymentSucceededEvent.EVENT_TYPE: {
                    PaymentSucceededEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentSucceededEvent.class);
                    log.info("Inventory: received PaymentSucceededEvent for order: {}", event.getOrderId());
                    inventoryService.commit(event.getOrderId());
                    break;
                }
                case PaymentSessionExpiredEvent.EVENT_TYPE: {
                    PaymentSessionExpiredEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentSessionExpiredEvent.class);
                    log.info("Inventory: received PaymentSessionExpiredEvent for order: {}", event.getOrderId());
                    inventoryService.release(event.getOrderId());
                    break;
                }
                case PaymentFailedEvent.EVENT_TYPE: {
                    PaymentFailedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentFailedEvent.class);
                    log.info("Inventory: received PaymentFailedEvent for order: {}", event.getOrderId());
                    inventoryService.release(event.getOrderId());
                    break;
                }
                default:
                    // ignore unknown payment event types
            }
        } catch (RuntimeException e) {
            log.warn("Idempotency: inventory state transition already applied for payment event {} (possible replay). Skipping. Detail: {}",
                    envelope.getEventType(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment event: {}", envelope.getEventType(), e);
        }
    }

    // ── cart.events ───────────────────────────────────────────────────────

    @Transactional
    public void handleCartEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (CartCheckoutInitiatedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            CartCheckoutInitiatedEvent event = objectMapper.convertValue(
                    envelope.getPayload(), CartCheckoutInitiatedEvent.class);
            log.info("Inventory: received CartCheckoutInitiatedEvent for cart: {}", event.getCartId());
            try {
                java.util.Map<String, Integer> items = new java.util.HashMap<>();
                event.getItems().forEach(item -> items.put(item.getProductId(), item.getQuantity()));

                inventoryService.reserveForOrder(event.getCartId(), items);

                log.info("Stock reserved for cart: {}", event.getCartId());
                publishAfterCommit(KafkaTopics.INVENTORY_EVENTS, event.getCartId(),
                        new InventoryEvent(event.getCartId(), InventoryEvent.STOCK_RESERVED));
            } catch (IllegalStateException e) {
                log.warn("Idempotency: stock already reserved for cart {} (possible replay). Skipping. Detail: {}",
                        event.getCartId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to reserve stock for cart: {}, error: {}",
                        event.getCartId(), e.getMessage());
                publish(KafkaTopics.INVENTORY_EVENTS, event.getCartId(),
                        new InventoryEvent(event.getCartId(), InventoryEvent.STOCK_FAILED));
            }
        } else if (CartAbandonedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            CartAbandonedEvent event = objectMapper.convertValue(
                    envelope.getPayload(), CartAbandonedEvent.class);
            log.info("Inventory: received CartAbandonedEvent for cart: {}", event.getCartId());
            try {
                inventoryService.release(event.getCartId());
            } catch (RuntimeException e) {
                log.warn("Idempotency: stock already released for cart {} (possible replay). Skipping. Detail: {}",
                        event.getCartId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to release stock for abandoned cart: {}", event.getCartId(), e);
            }
        }
    }

    // ── inventory.events ──────────────────────────────────────────────────

    @Transactional
    public void handleInventoryEvent(InventoryEvent event) {
        if (InventoryEvent.RELEASE_STOCK.equals(event.getEventType())) {
            log.info("Inventory: received release-stock command for order: {}", event.getOrderId());
            try {
                inventoryService.release(event.getOrderId());
                publishAfterCommit(KafkaTopics.INVENTORY_EVENTS, event.getOrderId(),
                        new InventoryEvent(event.getOrderId(), InventoryEvent.STOCK_RELEASED));
            } catch (RuntimeException e) {
                log.warn("Idempotency: stock already released for order {} (possible replay). Skipping. Detail: {}",
                        event.getOrderId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to release stock for order: {}", event.getOrderId(), e);
            }
        }
    }

    // ── product.events ────────────────────────────────────────────────────

    public void handleProductEvent(ProductCreatedEvent event) {
        if (!ProductCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Inventory: received ProductCreatedEvent for product: {}", event.getProductId());

        if (inventoryItemQueryAdapter.findByProductId(event.getProductId()) != null) {
            log.warn("Idempotency: inventory item already exists for product {} (possible replay). Skipping.",
                    event.getProductId());
            return;
        }

        try {
            InventoryItem item = new InventoryItem();
            item.setProductId(event.getProductId());
            item.setQuantity(event.getInitialQuantity());
            item.setReservedQuantity(0);
            item.setLowStockThreshold(10);

            // Create through STM — sets initial state (STOCK_PENDING) and persists via EntityStore
            inventoryStateEntityService.process(item, null, null);
            log.info("Initialized inventory for product: {} with quantity: {}",
                    event.getProductId(), event.getInitialQuantity());

            ProductInventoryInitializedEvent completionEvent = new ProductInventoryInitializedEvent(
                    event.getProductId(),
                    java.time.LocalDateTime.now());
            publish(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), completionEvent,
                    Map.of("eventType", ProductInventoryInitializedEvent.EVENT_TYPE));
        } catch (RuntimeException e) {
            log.warn("Idempotency: inventory initialization for product {} already completed (possible replay). Skipping. Detail: {}",
                    event.getProductId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to initialize inventory for product: {}", event.getProductId(), e);
        }
    }

    // ── Publishing helpers ────────────────────────────────────────────────

    private void publish(String topic, String key, Object payload) {
        publish(topic, key, payload, Map.of());
    }

    private void publish(String topic, String key, Object payload, Map<String, String> extraHeaders) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            java.util.Map<String, Object> headers = new java.util.HashMap<>();
            headers.put("key", key);
            headers.putAll(extraHeaders);
            chenilePub.publish(topic, body, headers);
        } catch (JacksonException e) {
            log.error("Failed to serialize event for topic={}, key={}", topic, key, e);
        }
    }

    private void publishAfterCommit(String topic, String key, Object payload) {
        Runnable publishAction = () -> publish(topic, key, payload);

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
