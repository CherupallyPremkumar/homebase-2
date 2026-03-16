package com.homebase.ecom.cart.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * Chenile event handler for cart cross-service events.
 * Registered via cartEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * Bean name "cartEventService" must match the service JSON id.
 */
public class CartEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CartEventHandler.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public CartEventHandler(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── inventory.events ────────────────────────────────────────────────

    @Transactional
    public void handleInventoryEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        switch (envelope.getEventType()) {
            case InventoryEvent.STOCK_RESERVED: {
                InventoryEvent event = objectMapper.convertValue(
                        envelope.getPayload(), InventoryEvent.class);
                log.info("Cart: stock reserved for order/cart: {}", event.getOrderId());
                break;
            }
            case InventoryEvent.STOCK_FAILED: {
                InventoryEvent event = objectMapper.convertValue(
                        envelope.getPayload(), InventoryEvent.class);
                log.warn("Cart: stock reservation FAILED for order/cart: {} — items may be unavailable",
                        event.getOrderId());
                // TODO: query cart by orderId and flag items as unavailable
                // once CartJpaRepository.findByCustomerIdAndStateIn() is available
                break;
            }
            case StockDepletedEvent.EVENT_TYPE: {
                StockDepletedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), StockDepletedEvent.class);
                log.warn("Cart: stock depleted for product: {} — active carts with this item should be flagged",
                        event.getProductId());
                // TODO: query active carts containing this variantId and flag items
                break;
            }
            default:
                log.debug("Cart: ignoring inventory event type: {}", envelope.getEventType());
        }
    }

    // ── product.events ──────────────────────────────────────────────────

    @Transactional
    public void handleProductEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        switch (envelope.getEventType()) {
            case "PRODUCT_PRICE_CHANGED": {
                ProductPriceChangedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), ProductPriceChangedEvent.class);
                log.info("Cart: price changed for product: {} to {}",
                        event.getProductId(), event.getNewPrice());
                // TODO: query active carts containing this variantId and update unitPrice
                // once CartJpaRepository.findActiveCartsByVariantId() is available
                break;
            }
            case ProductDiscontinuedEvent.EVENT_TYPE: {
                ProductDiscontinuedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), ProductDiscontinuedEvent.class);
                log.warn("Cart: product discontinued: {} reason: {} — should remove from active carts",
                        event.getProductId(), event.getReason());
                // TODO: query active carts and remove this variant, recalculate subtotals
                break;
            }
            default:
                log.debug("Cart: ignoring product event type: {}", envelope.getEventType());
        }
    }

    // ── Publishing helpers ──────────────────────────────────────────────

    void publish(String topic, String key, Object payload, Map<String, String> extraHeaders) {
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

    void publishAfterCommit(String topic, String key, Object payload, Map<String, String> extraHeaders) {
        Runnable publishAction = () -> publish(topic, key, payload, extraHeaders);

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
