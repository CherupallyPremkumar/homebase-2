package com.homebase.ecom.fulfillment.service.consumer;

import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.shared.event.KafkaTopics;

import java.util.Map;

/**
 * Kafka consumer that listens to ORDER_EVENTS topic and triggers the fulfillment
 * saga when an order transitions to PAID state.
 * Creates a new FulfillmentSaga entity (entering INITIATED state via STM)
 * and then triggers the reserveInventory event to start the saga.
 */
@Component
public class OrderPaidEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderPaidEventConsumer.class);

    @Autowired
    @Qualifier("_fulfillmentStateEntityService_")
    private StateEntityServiceImpl<FulfillmentSaga> fulfillmentStateEntityService;

    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "fulfillment-event-group")
    public void onOrderEvent(Map<String, Object> event) {
        String eventType = (String) event.get("eventType");
        if (!"ORDER_PAID".equals(eventType)) {
            return;
        }

        String orderId = (String) event.get("orderId");
        String customerId = (String) event.get("customerId");
        log.info("Received ORDER_PAID event for order: {}, customer: {}", orderId, customerId);

        try {
            // Create a new FulfillmentSaga entity (enters INITIATED state)
            FulfillmentSaga saga = new FulfillmentSaga();
            saga.setOrderId(orderId);
            saga.setUserId(customerId);
            fulfillmentStateEntityService.create(saga);

            log.info("Fulfillment saga created for order: {}, sagaId: {}", orderId, saga.getId());
        } catch (Exception e) {
            log.error("Failed to create fulfillment saga for order: {}", orderId, e);
            // TODO: Publish failure event or send to dead-letter topic for retry
        }
    }
}
