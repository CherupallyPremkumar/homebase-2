package com.homebase.ecom.shipping.service.event;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OrderDeliveredEvent;
import com.homebase.ecom.shared.event.OrderShippedEvent;
import com.homebase.ecom.shared.event.ReturnInitiatedEvent;
import com.homebase.ecom.shipping.model.Shipping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Publishes shipping domain events to Kafka.
 * Used by PostSaveHooks after state transitions are persisted.
 */
public class ShippingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ShippingEventPublisher.class);

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes event when a shipment is created and enters AWAITING_PICKUP.
     */
    public void publishShipmentCreated(Shipping shipping) {
        log.info("ShipmentCreatedEvent: shipment={}, order={}, carrier={}",
                shipping.getId(), shipping.getOrderId(), shipping.getCarrier());

        if (kafkaTemplate != null) {
            OrderShippedEvent event = new OrderShippedEvent(
                    shipping.getOrderId(),
                    shipping.getCarrier(),
                    shipping.getTrackingNumber(),
                    toLocalDateTime(shipping.getEstimatedDelivery()),
                    LocalDateTime.now()
            );
            kafkaTemplate.send(KafkaTopics.SHIPPING_EVENTS, shipping.getOrderId(), event);
        }
    }

    /**
     * Publishes event when shipment enters IN_TRANSIT state.
     */
    public void publishShipmentInTransit(Shipping shipping) {
        log.info("ShipmentInTransitEvent: shipment={}, order={}, location={}",
                shipping.getId(), shipping.getOrderId(), shipping.getCurrentLocation());

        if (kafkaTemplate != null) {
            OrderShippedEvent event = new OrderShippedEvent(
                    shipping.getOrderId(),
                    shipping.getCarrier(),
                    shipping.getTrackingNumber(),
                    toLocalDateTime(shipping.getEstimatedDelivery()),
                    toLocalDateTime(shipping.getShippedAt())
            );
            kafkaTemplate.send(KafkaTopics.SHIPPING_EVENTS, shipping.getOrderId(), event);
        }
    }

    /**
     * Publishes event when shipment is delivered.
     */
    public void publishShipmentDelivered(Shipping shipping) {
        log.info("ShipmentDeliveredEvent: shipment={}, order={}, deliveredAt={}",
                shipping.getId(), shipping.getOrderId(), shipping.getDeliveredAt());

        if (kafkaTemplate != null) {
            OrderDeliveredEvent event = new OrderDeliveredEvent(
                    shipping.getOrderId(),
                    null, // customerId not available in shipping context
                    toLocalDateTime(shipping.getDeliveredAt())
            );
            kafkaTemplate.send(KafkaTopics.SHIPPING_EVENTS, shipping.getOrderId(), event);
        }
    }

    /**
     * Publishes event when a return shipment is created.
     */
    public void publishReturnShipmentCreated(Shipping shipping) {
        log.info("ReturnShipmentCreatedEvent: shipment={}, order={}, returnReason={}",
                shipping.getId(), shipping.getOrderId(), shipping.getReturnReason());

        if (kafkaTemplate != null) {
            ReturnInitiatedEvent event = new ReturnInitiatedEvent(
                    shipping.getOrderId(),
                    null, // customerId not available in shipping context
                    shipping.getReturnReason(),
                    LocalDateTime.now(),
                    null // returnedItems not tracked at shipping level
            );
            kafkaTemplate.send(KafkaTopics.SHIPPING_EVENTS, shipping.getOrderId(), event);
        }
    }

    private LocalDateTime toLocalDateTime(java.util.Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
