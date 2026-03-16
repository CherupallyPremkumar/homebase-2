package com.homebase.ecom.user.service.event;

import com.homebase.ecom.shared.event.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chenile event handler for user cross-service events.
 * Registered via userEventService.json -- operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription.
 *
 * User service is primarily a PRODUCER of events (USER_REGISTERED, USER_VERIFIED,
 * USER_SUSPENDED, USER_DEACTIVATED). It has minimal consumption needs.
 *
 * Bean name "userEventService" -- must match the service JSON id.
 */
public class UserEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UserEventHandler.class);

    /**
     * Handles order events -- minimal consumption.
     * Example: when an order is placed for a user, we may want to update lastOrderAt
     * or track order count for loyalty. Currently a no-op placeholder.
     */
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        log.debug("User event handler: received order event type={}", envelope.getEventType());
        // Minimal consumer -- user module is primarily a producer.
        // Future: track order counts, loyalty points, etc.
    }
}
