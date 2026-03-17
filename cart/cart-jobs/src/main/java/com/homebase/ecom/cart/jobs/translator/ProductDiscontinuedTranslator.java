package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductDiscontinuedEvent;
import org.chenile.pubsub.ChenilePub;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * Translates PRODUCT_DISCONTINUED from product.events into flagUnavailable cart events.
 *
 * product.events/PRODUCT_DISCONTINUED {productId}
 *   → query cart-query: findActiveCartsWithVariant(productId)
 *   → fan-out: publish {cartId, "flagUnavailable", {productId}} to cart.events
 *
 * Note: ProductDiscontinuedEvent has productId (all variants discontinued).
 * Query finds carts by any variant of that product.
 */
public class ProductDiscontinuedTranslator extends AbstractCartEventTranslator {

    private final CartQueryPort cartQueryPort;

    public ProductDiscontinuedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        super(chenilePub);
        this.cartQueryPort = cartQueryPort;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "cart-jobs-discontinued",
            containerFactory = "kafkaListenerContainerFactory")
    public void onProductDiscontinued(ProductDiscontinuedEvent event) {
        if (event.getProductId() == null) {
            log.warn("ProductDiscontinuedEvent missing productId, skipping");
            return;
        }

        log.info("Product discontinued: {}", event.getProductId());

        // For discontinued, we flag all variants as unavailable
        // CartQueryPort needs a productId-level query
        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(event.getProductId());

        String payload = "{\"productId\":\"" + event.getProductId() + "\"}";
        fanOut(cartIds, "flagUnavailable", payload);
    }
}
