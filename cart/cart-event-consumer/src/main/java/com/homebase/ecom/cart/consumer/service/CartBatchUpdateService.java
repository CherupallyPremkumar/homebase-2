package com.homebase.ecom.cart.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for performing batch updates on cart items based on external events.
 *
 * In the refactored cart BC, inventory and pricing events are handled by
 * CartEventHandler (chenile-kafka) which processes STOCK_RESERVED and
 * STOCK_FAILED events from inventory.events topic. Direct cart item
 * updates (price changes, stock status) are no longer done at the
 * infrastructure level -- they flow through the STM.
 */
@Service
public class CartBatchUpdateService {

    private static final Logger log = LoggerFactory.getLogger(CartBatchUpdateService.class);

    /**
     * Placeholder for future batch operations that may be needed.
     * Individual event handling is done by CartEventHandler via chenile-kafka.
     */
    public void logEventReceived(String eventType, String productId) {
        log.info("Received {} event for product {}", eventType, productId);
    }
}
