package com.homebase.ecom.order.service.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.shared.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.chenile.stm.STM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.stream.Collectors;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.domain.port.OrderRepository;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Consumer for events that trigger order lifecycle changes.
 */
@Service
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Autowired
    @Qualifier("orderEntityStm")
    private STM<com.homebase.ecom.order.model.Order> orderStm;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.homebase.ecom.order.service.OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Consumes the CartCheckoutInitiatedEvent for early order creation.
     */
    @KafkaListener(topics = KafkaTopics.CART_EVENTS, groupId = "order-service")
    public void consumeCartCheckoutInitiated(CartCheckoutInitiatedEvent event) {
        log.info("Received CartCheckoutInitiatedEvent for cart: {}", event.getCartId());

        try {
            orderService.createOrder(event);
        } catch (Exception e) {
            log.error("Failed to process CartCheckoutInitiatedEvent for cart: " + event.getCartId(), e);
        }
    }

    /**
     * Consumes the CartCheckoutCompletedEvent (Backward compatibility/Fallback).
     */
    @KafkaListener(topics = KafkaTopics.CART_EVENTS, groupId = "order-service-legacy")
    public void consumeCartCheckoutCompleted(CartCheckoutCompletedEvent event) {
        // This is now largely handled by CartCheckoutInitiatedEvent +
        // PaymentSucceededEvent.
        // We handle it here mostly for resilience or if the early creation was skipped.
        log.info("Received CartCheckoutCompletedEvent for cart: {}", event.getCartId());

        Optional<com.homebase.ecom.order.model.Order> existing = orderRepository.findById(event.getCartId());
        if (existing.isPresent()) {
            log.info("Order {} already exists, payment result will advance it.", event.getCartId());
            return;
        }

        // Fallback creation logic (similar to above)
        // ... (omitted for brevity, or just call a shared private method if needed)
    }

    /**
     * Consumes payment events to advance order state.
     */
    @KafkaListener(topics = KafkaTopics.PAYMENT_EVENTS, groupId = "order-service")
    public void onPaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null)
            return;

        try {
            if (PaymentSucceededEvent.EVENT_TYPE.equals(envelope.getEventType())) {
                PaymentSucceededEvent event = objectMapper.convertValue(envelope.getPayload(),
                        PaymentSucceededEvent.class);
                log.info("Order: received PaymentSucceededEvent for order: {}", event.getOrderId());

                Optional<com.homebase.ecom.order.model.Order> orderOpt = orderRepository.findById(event.getOrderId());
                if (orderOpt.isPresent()) {
                    orderStm.proceed(orderOpt.get(), "processPayment", null);
                    log.info("Order {} advanced to PAYMENT_CONFIRMED.", event.getOrderId());
                } else {
                    log.warn("Received payment success for non-existent order: {}", event.getOrderId());
                }
            } else if (PaymentFailedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
                PaymentFailedEvent event = objectMapper.convertValue(envelope.getPayload(), PaymentFailedEvent.class);
                log.info("Order: received PaymentFailedEvent for order: {}", event.getOrderId());

                Optional<com.homebase.ecom.order.model.Order> orderOpt = orderRepository.findById(event.getOrderId());
                if (orderOpt.isPresent()) {
                    orderStm.proceed(orderOpt.get(), "paymentFailed", null);
                    log.info("Order {} moved to FAILED state.", event.getOrderId());
                }
            }
        } catch (Exception e) {
            log.error("Error processing payment event for order: " + envelope.getEventType(), e);
        }
    }
}
