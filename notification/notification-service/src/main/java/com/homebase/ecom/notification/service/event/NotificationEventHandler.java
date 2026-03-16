package com.homebase.ecom.notification.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Chenile event handler for notification cross-service events.
 * Registered via notificationEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription.
 *
 * CONSUMES:
 *   - order.events: ORDER_CREATED, ORDER_SHIPPED, ORDER_DELIVERED
 *   - payment.events: PAYMENT_SUCCEEDED, PAYMENT_FAILED
 *   - return.events: RETURN_APPROVED (via ReturnInitiatedEvent)
 *   - review.events: REVIEW_PUBLISHED
 *
 * For each consumed event, creates a Notification entity and queues it for delivery via STM.
 */
public class NotificationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventHandler.class);

    private final StateEntityServiceImpl<Notification> notificationStateEntityService;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public NotificationEventHandler(
            @Qualifier("_notificationStateEntityService_") StateEntityServiceImpl<Notification> notificationStateEntityService,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.notificationStateEntityService = notificationStateEntityService;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── order.events ──────────────────────────────────────────────────────

    @Transactional
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            switch (envelope.getEventType()) {
                case "ORDER_CREATED": {
                    OrderCreatedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), OrderCreatedEvent.class);
                    log.info("Notification: received ORDER_CREATED for order: {}", event.getOrderId());
                    createNotification(
                            event.getCustomerId(),
                            "EMAIL",
                            "ORDER_CONFIRMATION",
                            "Order Confirmed: " + event.getOrderId(),
                            "Your order " + event.getOrderId() + " has been confirmed.",
                            Map.of("orderId", event.getOrderId(), "eventType", "ORDER_CREATED")
                    );
                    break;
                }
                case "ORDER_SHIPPED": {
                    OrderShippedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), OrderShippedEvent.class);
                    log.info("Notification: received ORDER_SHIPPED for order: {}", event.getOrderId());
                    createNotification(
                            null, // customerId not on OrderShippedEvent — resolved by downstream
                            "EMAIL",
                            "ORDER_SHIPPED",
                            "Your order has shipped: " + event.getOrderId(),
                            "Your order " + event.getOrderId() + " has been shipped via "
                                    + event.getCarrier() + ". Tracking: " + event.getTrackingNumber(),
                            Map.of("orderId", event.getOrderId(), "eventType", "ORDER_SHIPPED",
                                    "carrier", event.getCarrier() != null ? event.getCarrier() : "",
                                    "trackingNumber", event.getTrackingNumber() != null ? event.getTrackingNumber() : "")
                    );
                    break;
                }
                case "ORDER_DELIVERED": {
                    OrderDeliveredEvent event = objectMapper.convertValue(
                            envelope.getPayload(), OrderDeliveredEvent.class);
                    log.info("Notification: received ORDER_DELIVERED for order: {}", event.getOrderId());
                    createNotification(
                            event.getCustomerId(),
                            "EMAIL",
                            "ORDER_DELIVERED",
                            "Your order has been delivered: " + event.getOrderId(),
                            "Your order " + event.getOrderId() + " has been successfully delivered.",
                            Map.of("orderId", event.getOrderId(), "eventType", "ORDER_DELIVERED")
                    );
                    break;
                }
                default:
                    // ignore unknown order event types
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", envelope.getEventType(), e);
        }
    }

    // ── payment.events ────────────────────────────────────────────────────

    @Transactional
    public void handlePaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            switch (envelope.getEventType()) {
                case PaymentSucceededEvent.EVENT_TYPE: {
                    PaymentSucceededEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentSucceededEvent.class);
                    log.info("Notification: received PAYMENT_SUCCEEDED for order: {}", event.getOrderId());
                    createNotification(
                            null,
                            "EMAIL",
                            "PAYMENT_CONFIRMATION",
                            "Payment confirmed for order: " + event.getOrderId(),
                            "Your payment for order " + event.getOrderId() + " has been confirmed.",
                            Map.of("orderId", event.getOrderId(), "eventType", "PAYMENT_SUCCEEDED")
                    );
                    break;
                }
                case PaymentFailedEvent.EVENT_TYPE: {
                    PaymentFailedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentFailedEvent.class);
                    log.info("Notification: received PAYMENT_FAILED for order: {}", event.getOrderId());
                    createNotification(
                            null,
                            "EMAIL",
                            "PAYMENT_FAILED",
                            "Payment failed for order: " + event.getOrderId(),
                            "Your payment for order " + event.getOrderId() + " has failed. Reason: "
                                    + event.getFailureReason(),
                            Map.of("orderId", event.getOrderId(), "eventType", "PAYMENT_FAILED")
                    );
                    break;
                }
                default:
                    // ignore unknown payment event types
            }
        } catch (Exception e) {
            log.error("Error processing payment event: {}", envelope.getEventType(), e);
        }
    }

    // ── return.events ─────────────────────────────────────────────────────

    @Transactional
    public void handleReturnEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            if ("RETURN_APPROVED".equals(envelope.getEventType())) {
                ReturnInitiatedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), ReturnInitiatedEvent.class);
                log.info("Notification: received RETURN_APPROVED for order: {}", event.getOrderId());
                createNotification(
                        event.getCustomerId(),
                        "EMAIL",
                        "RETURN_APPROVED",
                        "Return approved for order: " + event.getOrderId(),
                        "Your return request for order " + event.getOrderId() + " has been approved.",
                        Map.of("orderId", event.getOrderId(), "eventType", "RETURN_APPROVED")
                );
            }
        } catch (Exception e) {
            log.error("Error processing return event: {}", envelope.getEventType(), e);
        }
    }

    // ── review.events ─────────────────────────────────────────────────────

    @Transactional
    public void handleReviewEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            if ("REVIEW_PUBLISHED".equals(envelope.getEventType())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> payload = objectMapper.convertValue(envelope.getPayload(), Map.class);
                String productId = payload.get("productId") != null ? payload.get("productId").toString() : "";
                String customerId = payload.get("customerId") != null ? payload.get("customerId").toString() : null;
                log.info("Notification: received REVIEW_PUBLISHED for product: {}", productId);
                createNotification(
                        customerId,
                        "IN_APP",
                        "REVIEW_PUBLISHED",
                        "Your review has been published",
                        "Your review for product " + productId + " has been published. Thank you for your feedback!",
                        Map.of("productId", productId, "eventType", "REVIEW_PUBLISHED")
                );
            }
        } catch (Exception e) {
            log.error("Error processing review event: {}", envelope.getEventType(), e);
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────

    private void createNotification(String customerId, String channel, String templateId,
                                     String subject, String body, Map<String, String> metadata) {
        try {
            Notification notification = new Notification();
            notification.setCustomerId(customerId);
            notification.setChannel(channel);
            notification.setTemplateId(templateId);
            notification.setSubject(subject);
            notification.setBody(body);
            notification.setMetadata(metadata != null ? new HashMap<>(metadata) : new HashMap<>());

            // Create through STM — sets initial state (CREATED) and persists via EntityStore
            notificationStateEntityService.process(notification, null, null);

            log.info("Notification created: id={}, customerId={}, channel={}, templateId={}",
                    notification.getId(), customerId, channel, templateId);
        } catch (Exception e) {
            log.error("Failed to create notification: customerId={}, templateId={}", customerId, templateId, e);
        }
    }
}
