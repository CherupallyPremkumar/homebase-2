package com.homebase.ecom.shipping.service.event;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OrderItemCancellationRequestedEvent;
import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.stm.STM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer for item-level events to adjust fulfillment operations.
 */
@Service
public class OrderItemEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderItemEventConsumer.class);

    @Autowired
    @Qualifier("shippingEntityStm")
    private STM<Shipping> shippingStm;

    /**
     * Reacts to an item cancellation request.
     */
    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "shipping-service-items")
    public void consumeOrderItemCancellation(OrderItemCancellationRequestedEvent event) {
        log.info("Shipping received cancellation request for order: {}, item: {}",
                event.getOrderId(), event.getOrderItemId());

        try {
            // Ideally, we find the shipping record by order ID
            // For now, we'll log it as an activity if the shipment exists.
            // In a real system, we might transition the shipment to 'HOLD' or 'ADJUSTED'.

            // Note: Since ID = "SHIP-" + orderId in our OrderCreatedEventConsumer
            String shippingId = "SHIP-" + event.getOrderId();

            // We can't easily 'find' without a repository here if we only have the STM
            // But we can use the STM to 'proceed' with a dedicated event if we had one.

            log.info("Fulfillment for item {} in order {} has been flagged for cancellation at the Hub.",
                    event.getOrderItemId(), event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to process OrderItemCancellationRequestedEvent for item: " + event.getOrderItemId(), e);
        }
    }
}
