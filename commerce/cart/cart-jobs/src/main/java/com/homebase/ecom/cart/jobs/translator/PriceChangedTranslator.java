package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductPriceChangedEvent;
import org.chenile.pubsub.ChenilePub;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * Translates PRICE_CHANGED from product.events into refreshPricing cart events.
 *
 * product.events/PRICE_CHANGED {variantId}
 *   → query cart-query: findActiveCartsWithVariant(variantId)
 *   → fan-out: publish {cartId, "refreshPricing", {}} to cart.events
 */
public class PriceChangedTranslator extends AbstractCartEventTranslator {

    private final CartQueryPort cartQueryPort;

    public PriceChangedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        super(chenilePub);
        this.cartQueryPort = cartQueryPort;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "cart-jobs",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPriceChanged(ProductPriceChangedEvent event) {
        if (event.getVariantId() == null) {
            log.warn("ProductPriceChangedEvent missing variantId, skipping");
            return;
        }

        log.info("Price changed for variantId={}", event.getVariantId());

        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(event.getVariantId());

        fanOut(cartIds, "refreshPricing", "{}");
    }
}
