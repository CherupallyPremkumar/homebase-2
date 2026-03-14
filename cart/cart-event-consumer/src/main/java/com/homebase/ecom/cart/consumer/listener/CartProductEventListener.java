package com.homebase.ecom.cart.consumer.listener;

import com.homebase.ecom.cart.consumer.service.CartBatchUpdateService;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductPriceChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CartProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(CartProductEventListener.class);
    private final CartBatchUpdateService cartBatchUpdateService;

    public CartProductEventListener(CartBatchUpdateService cartBatchUpdateService) {
        this.cartBatchUpdateService = cartBatchUpdateService;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "cart-event-group")
    public void onPriceChanged(ProductPriceChangedEvent event) {
        log.info("Cart: received ProductPriceChangedEvent for product: {}", event.getProductId());
        try {
            cartBatchUpdateService.processPriceChange(event);
            log.info("Cart: successfully updated prices for product: {}", event.getProductId());
        } catch (Exception e) {
            log.error("Cart: failed to update prices for product: {}", event.getProductId(), e);
        }
    }
}
