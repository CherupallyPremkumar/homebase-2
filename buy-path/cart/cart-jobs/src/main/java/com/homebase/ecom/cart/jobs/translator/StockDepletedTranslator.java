package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.StockDepletedEvent;
import org.chenile.pubsub.ChenilePub;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * Translates STOCK_DEPLETED from inventory.events into flagUnavailable cart events.
 *
 * inventory.events/STOCK_DEPLETED {variantId}
 *   → query cart-query: findActiveCartsWithVariant(variantId)
 *   → fan-out: publish {cartId, "flagUnavailable", {variantId}} to cart.events
 */
public class StockDepletedTranslator extends AbstractCartEventTranslator {

    private final CartQueryPort cartQueryPort;

    public StockDepletedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        super(chenilePub);
        this.cartQueryPort = cartQueryPort;
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_EVENTS, groupId = "cart-jobs",
            containerFactory = "kafkaListenerContainerFactory")
    public void onStockDepleted(StockDepletedEvent event) {
        if (event.getVariantId() == null) {
            log.warn("StockDepletedEvent missing variantId, skipping");
            return;
        }

        log.info("Stock depleted for variantId={}", event.getVariantId());

        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(event.getVariantId());

        String payload = "{\"variantId\":\"" + event.getVariantId() + "\"}";
        fanOut(cartIds, "flagUnavailable", payload);
    }
}
