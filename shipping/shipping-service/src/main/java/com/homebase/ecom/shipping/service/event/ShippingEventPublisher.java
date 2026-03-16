package com.homebase.ecom.shipping.service.event;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Publishes shipping domain events to Kafka via ChenilePub.
 * PUBLISHES to shipping.events: SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, DELIVERY_FAILED, RETURNED
 */
public class ShippingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ShippingEventPublisher.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    /**
     * Publishes SHIPPED event when label is created and shipment enters LABEL_CREATED.
     * Notifies customer + updates order status.
     */
    public void publishShipped(Shipping shipping) {
        log.info("ShippedEvent: shipment={}, order={}, carrier={}, tracking={}",
                shipping.getId(), shipping.getOrderId(), shipping.getCarrier(), shipping.getTrackingNumber());
        publishEvent(shipping, "SHIPPED");
    }

    /**
     * Publishes IN_TRANSIT event when shipment enters transit.
     */
    public void publishInTransit(Shipping shipping) {
        log.info("InTransitEvent: shipment={}, order={}, location={}",
                shipping.getId(), shipping.getOrderId(), shipping.getCurrentLocation());
        publishEvent(shipping, "IN_TRANSIT");
    }

    /**
     * Publishes OUT_FOR_DELIVERY event.
     */
    public void publishOutForDelivery(Shipping shipping) {
        log.info("OutForDeliveryEvent: shipment={}, order={}",
                shipping.getId(), shipping.getOrderId());
        publishEvent(shipping, "OUT_FOR_DELIVERY");
    }

    /**
     * Publishes DELIVERED event when shipment is delivered.
     * Notifies customer + signals order completion.
     */
    public void publishDelivered(Shipping shipping) {
        log.info("DeliveredEvent: shipment={}, order={}, deliveredAt={}",
                shipping.getId(), shipping.getOrderId(), shipping.getActualDeliveryDate());
        publishEvent(shipping, "DELIVERED");
    }

    /**
     * Publishes DELIVERY_FAILED event when delivery attempt fails.
     * Notifies customer of failed attempt.
     */
    public void publishDeliveryFailed(Shipping shipping) {
        log.info("DeliveryFailedEvent: shipment={}, order={}, attempts={}",
                shipping.getId(), shipping.getOrderId(), shipping.getDeliveryAttempts());
        publishEvent(shipping, "DELIVERY_FAILED");
    }

    /**
     * Publishes RETURNED event when shipment is returned to warehouse.
     */
    public void publishReturned(Shipping shipping) {
        log.info("ReturnedEvent: shipment={}, order={}",
                shipping.getId(), shipping.getOrderId());
        publishEvent(shipping, "RETURNED");
    }

    // ── Internal ──

    private void publishEvent(Shipping shipping, String eventType) {
        if (chenilePub == null || objectMapper == null) return;

        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", eventType);
            event.put("shipmentId", shipping.getId());
            event.put("orderId", shipping.getOrderId());
            event.put("customerId", shipping.getCustomerId());
            event.put("carrier", shipping.getCarrier());
            event.put("trackingNumber", shipping.getTrackingNumber());
            event.put("shippingMethod", shipping.getShippingMethod());
            event.put("currentLocation", shipping.getCurrentLocation());
            event.put("deliveryAttempts", shipping.getDeliveryAttempts());
            event.put("estimatedDeliveryDate", shipping.getEstimatedDeliveryDate());
            event.put("actualDeliveryDate", shipping.getActualDeliveryDate());

            String body = objectMapper.writeValueAsString(event);
            Map<String, Object> headers = new HashMap<>();
            headers.put("key", shipping.getOrderId());
            headers.put("eventType", eventType);
            chenilePub.publish(KafkaTopics.SHIPPING_EVENTS, body, headers);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} event for shipment={}", eventType, shipping.getId(), e);
        }
    }
}
