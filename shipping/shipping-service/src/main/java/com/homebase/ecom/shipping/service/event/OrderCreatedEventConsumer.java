package com.homebase.ecom.shipping.service.event;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OrderCreatedEvent;
import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.stm.STM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Consumer for OrderCreatedEvent to initiate shipment fulfillment.
 */
@Service
public class OrderCreatedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventConsumer.class);

    @Autowired
    @Qualifier("shippingEntityStm")
    private STM<Shipping> shippingStm;

    /**
     * Consumes the OrderCreatedEvent and creates a corresponding shipping record.
     */
    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "shipping-service")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order: {}", event.getOrderId());

        try {
            Shipping shipping = new Shipping();
            // Use order ID in shipping ID for traceability
            shipping.setId("SHIP-" + event.getOrderId());
            shipping.setOrderId(event.getOrderId());

            // Stub initial carrier and tracking info (in reality this might come from a
            // 3PL)
            shipping.setCarrier("HOMEBASE-LOGISTICS");
            shipping.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

            // 1. Initialize the shipping (moves to initial state AWAITING_PICKUP in
            // shipping-states.xml)
            shippingStm.proceed(shipping, null, null);

            log.info("Shipping record {} successfully created for order {}.", shipping.getId(), event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvent for order: " + event.getOrderId(), e);
        }
    }
}
