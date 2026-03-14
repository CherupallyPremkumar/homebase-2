package com.homebase.ecom.cart.consumer.listener;

import com.homebase.ecom.cart.consumer.service.CartBatchUpdateService;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductStockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
            cartBatchUpdateService.processStockUpdate(event);
            log.info("Cart: successfully updated stock status for product: {}", event.getProductId());
        } catch (Exception e) {
            log.error("Cart: failed to update stock status for product: {}", event.getProductId(), e);
        }
    }
}
