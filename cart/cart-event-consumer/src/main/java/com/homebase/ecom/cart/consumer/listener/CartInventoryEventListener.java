package com.homebase.ecom.cart.consumer.listener;

import com.homebase.ecom.cart.consumer.service.CartBatchUpdateService;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductStockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener for inventory events affecting carts.
 * Primary inventory event handling (STOCK_RESERVED, STOCK_FAILED) is done by
 * CartEventHandler via chenile-kafka. This listener handles supplementary
 * stock update notifications for cart item status tracking.
 */
@Component
public class CartInventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(CartInventoryEventListener.class);
    private final CartBatchUpdateService cartBatchUpdateService;

    public CartInventoryEventListener(CartBatchUpdateService cartBatchUpdateService) {
        this.cartBatchUpdateService = cartBatchUpdateService;
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_EVENTS, groupId = "cart-event-group")
    public void onStockUpdated(ProductStockUpdatedEvent event) {
        log.info("Cart: received ProductStockUpdatedEvent for product: {}", event.getProductId());
        try {
            cartBatchUpdateService.logEventReceived("STOCK_UPDATED", event.getProductId());
        } catch (Exception e) {
            log.error("Cart: failed to process stock update for product: {}", event.getProductId(), e);
        }
    }
}
