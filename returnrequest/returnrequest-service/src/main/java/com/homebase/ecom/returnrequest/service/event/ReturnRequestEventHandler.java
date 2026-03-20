package com.homebase.ecom.returnrequest.service.event;

import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.shared.event.OrderDeliveredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chenile event handler for return request cross-service events.
 * Registered via returnrequestEventService.json -- operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * CONSUMES:
 * - order.events: ORDER_DELIVERED -> enable return window for the order
 *
 * Bean name "returnrequestEventService" must match the service JSON id.
 */
public class ReturnRequestEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestEventHandler.class);

    /**
     * Handles order events - specifically ORDER_DELIVERED which enables the return window.
     * When an order is delivered, it becomes eligible for return requests.
     */
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if ("ORDER_DELIVERED".equals(envelope.getEventType())) {
            log.info("ReturnRequest: received ORDER_DELIVERED event, return window now open");
            // The return window is enforced at request time via orderDeliveryDate validation.
            // This handler can be extended to pre-create return eligibility records.
        } else {
            log.debug("ReturnRequest: ignoring order event type: {}", envelope.getEventType());
        }
    }
}
