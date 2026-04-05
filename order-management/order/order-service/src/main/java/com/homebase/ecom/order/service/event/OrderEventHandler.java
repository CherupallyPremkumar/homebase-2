package com.homebase.ecom.order.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.shared.event.*;
import org.chenile.stm.STM;
import org.chenile.utils.entity.service.EntityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Chenile event handler for order cross-service events.
 * Registered via orderEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription.
 *
 * CONSUMES:
 * - payment.events: PAYMENT_SUCCEEDED, PAYMENT_FAILED
 * - shipping.events: SHIPPED, DELIVERED
 * - cart.events: CHECKOUT_INITIATED
 */
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    @Autowired
    @Qualifier("orderEntityStm")
    private STM<Order> orderStm;

    @Autowired
    @Qualifier("orderEntityStore")
    private EntityStore<Order> orderEntityStore;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ── payment.events ────────────────────────────────────────────────────

    @Transactional
    public void handlePaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            switch (envelope.getEventType()) {
                case PaymentSucceededEvent.EVENT_TYPE: {
                    PaymentSucceededEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentSucceededEvent.class);
                    log.info("Order: received PAYMENT_SUCCEEDED for order: {}", event.getOrderId());
                    Order order = orderEntityStore.retrieve(event.getOrderId());
                    if (order != null) {
                        orderStm.proceed(order, "paymentSucceeded", null);
                        log.info("Order {} advanced to PAID.", event.getOrderId());
                    } else {
                        log.warn("Payment success for non-existent order: {}", event.getOrderId());
                    }
                    break;
                }
                case PaymentFailedEvent.EVENT_TYPE: {
                    PaymentFailedEvent event = objectMapper.convertValue(
                            envelope.getPayload(), PaymentFailedEvent.class);
                    log.info("Order: received PAYMENT_FAILED for order: {}", event.getOrderId());
                    Order order = orderEntityStore.retrieve(event.getOrderId());
                    if (order != null) {
                        orderStm.proceed(order, "paymentFailed", null);
                        log.info("Order {} moved to PAYMENT_FAILED.", event.getOrderId());
                    }
                    break;
                }
                default:
                    log.debug("Ignoring unknown payment event type: {}", envelope.getEventType());
            }
        } catch (RuntimeException e) {
            log.warn("Idempotency: order state transition already applied for payment event {} (possible replay). Detail: {}",
                    envelope.getEventType(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment event: {}", envelope.getEventType(), e);
        }
    }

    // ── shipping.events ───────────────────────────────────────────────────

    @Transactional
    public void handleShippingEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        try {
            switch (envelope.getEventType()) {
                case "SHIPPED": {
                    String orderId = extractOrderId(envelope);
                    if (orderId == null) return;
                    log.info("Order: received SHIPPED event for order: {}", orderId);
                    Order order = orderEntityStore.retrieve(orderId);
                    if (order != null) {
                        orderStm.proceed(order, "markShipped", null);
                        log.info("Order {} advanced to SHIPPED.", orderId);
                    }
                    break;
                }
                case "DELIVERED": {
                    String orderId = extractOrderId(envelope);
                    if (orderId == null) return;
                    log.info("Order: received DELIVERED event for order: {}", orderId);
                    Order order = orderEntityStore.retrieve(orderId);
                    if (order != null) {
                        orderStm.proceed(order, "markDelivered", null);
                        log.info("Order {} advanced to DELIVERED.", orderId);
                    }
                    break;
                }
                default:
                    log.debug("Ignoring unknown shipping event type: {}", envelope.getEventType());
            }
        } catch (RuntimeException e) {
            log.warn("Idempotency: order state transition already applied for shipping event {} (possible replay). Detail: {}",
                    envelope.getEventType(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing shipping event: {}", envelope.getEventType(), e);
        }
    }

    // ── cart.events ───────────────────────────────────────────────────────

    @Transactional
    public void handleCartEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        if (CartCheckoutInitiatedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            CartCheckoutInitiatedEvent event = objectMapper.convertValue(
                    envelope.getPayload(), CartCheckoutInitiatedEvent.class);
            log.info("Order: received CHECKOUT_INITIATED for cart: {}", event.getCartId());
            try {
                orderService.createOrder(event);
            } catch (RuntimeException e) {
                log.warn("Idempotency: order for cart {} already created (possible replay). Detail: {}",
                        event.getCartId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to process CHECKOUT_INITIATED for cart: {}", event.getCartId(), e);
            }
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private String extractOrderId(EventEnvelope envelope) {
        if (envelope.getPayload() instanceof java.util.Map) {
            Object orderId = ((java.util.Map<String, Object>) envelope.getPayload()).get("orderId");
            return orderId != null ? orderId.toString() : null;
        }
        return null;
    }
}
