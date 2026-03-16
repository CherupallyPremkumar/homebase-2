package com.homebase.ecom.review.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.review.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Chenile event handler for review cross-service events.
 * Registered via reviewEventService.json -- operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * Bean name "reviewEventService" -- must match the service JSON id.
 */
public class ReviewEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ReviewEventHandler.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    public ReviewEventHandler(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // -- order.events --------------------------------------------------------

    /**
     * Handles order events. When ORDER_DELIVERED is received, prompts a review request
     * notification to the customer.
     */
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if ("ORDER_DELIVERED".equals(envelope.getEventType())) {
            try {
                OrderDeliveredEvent event = objectMapper.convertValue(
                        envelope.getPayload(), OrderDeliveredEvent.class);
                log.info("Review: received ORDER_DELIVERED for order: {}, customer: {}",
                        event.getOrderId(), event.getCustomerId());

                // Prompt review request notification
                if (notificationPort != null) {
                    notificationPort.notifyCustomer(
                            event.getCustomerId(),
                            null, // no review yet
                            "REVIEW_REQUESTED",
                            "Your order " + event.getOrderId() + " has been delivered. " +
                            "We would love to hear your feedback!");
                }

                log.info("Review request prompted for order: {}, customer: {}",
                        event.getOrderId(), event.getCustomerId());
            } catch (Exception e) {
                log.error("Error processing ORDER_DELIVERED event: {}", e.getMessage(), e);
            }
        }
    }

    // -- Publishing helpers --------------------------------------------------

    private void publish(String topic, String key, Object payload, Map<String, String> extraHeaders) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            java.util.Map<String, Object> headers = new java.util.HashMap<>();
            headers.put("key", key);
            headers.putAll(extraHeaders);
            chenilePub.publish(topic, body, headers);
        } catch (JacksonException e) {
            log.error("Failed to serialize event for topic={}, key={}", topic, key, e);
        }
    }
}
