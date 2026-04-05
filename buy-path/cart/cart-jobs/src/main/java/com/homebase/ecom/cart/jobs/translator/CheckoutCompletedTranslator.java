package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.shared.event.CheckoutCompletedEvent;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Translates CHECKOUT_COMPLETED from checkout.events into completeCheckout cart event.
 *
 * checkout.events/CHECKOUT_COMPLETED {cartId, orderId}
 *   → publish {cartId, "completeCheckout", {orderId}} to cart.events
 *
 * No fan-out needed — 1:1 mapping (event already has cartId).
 */
public class CheckoutCompletedTranslator extends AbstractCartEventTranslator {

    public CheckoutCompletedTranslator(ChenilePub chenilePub) {
        super(chenilePub);
    }

    @KafkaListener(topics = KafkaTopics.CHECKOUT_EVENTS, groupId = "cart-jobs",
            containerFactory = "kafkaListenerContainerFactory")
    public void onCheckoutCompleted(CheckoutCompletedEvent event) {
        if (event.getCartId() == null || event.getOrderId() == null) {
            log.warn("CheckoutCompletedEvent missing cartId or orderId, skipping");
            return;
        }

        log.info("Checkout completed for cartId={}, orderId={}", event.getCartId(), event.getOrderId());

        String payload = "{\"orderId\":\"" + event.getOrderId() + "\"}";
        publishCartEvent(event.getCartId(), "completeCheckout", payload);
    }
}
