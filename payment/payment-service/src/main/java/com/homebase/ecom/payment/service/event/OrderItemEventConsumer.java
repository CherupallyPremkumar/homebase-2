package com.homebase.ecom.payment.service.event;

import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentTransaction;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentTransactionRepository;
import com.homebase.ecom.payment.service.impl.PaymentServiceImpl;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.OrderItemRefundRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Consumer for item-level events to trigger payment operations like refunds.
 * Wired as @Bean in PaymentConfiguration.
 */
public class OrderItemEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderItemEventConsumer.class);

    private final PaymentServiceImpl paymentService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public OrderItemEventConsumer(PaymentServiceImpl paymentService,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentService = paymentService;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    /**
     * Reacts to an item refund request.
     */
    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "payment-service-items")
    public void consumeOrderItemRefund(OrderItemRefundRequestedEvent event) {
        log.info("Payment received refund request for order: {}, item: {}, amount: {}",
                event.getOrderId(), event.getOrderItemId(), event.getRefundAmount());

        try {
            // 1. Find the parent payment transaction to get the gateway transaction ID
            PaymentTransaction tx = paymentTransactionRepository.findByOrderId(event.getOrderId())
                    .stream()
                    .filter(t -> t.getGatewayTransactionId() != null)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Original payment transaction not found for order: " + event.getOrderId()));

            // 2. Trigger the refund
            paymentService.processRefund(
                    event.getOrderId(),
                    tx.getGatewayTransactionId(),
                    event.getRefundAmount(),
                    event.getReason());

            log.info("Granular refund processed for item {} in order {}",
                    event.getOrderItemId(), event.getOrderId());

        } catch (DataIntegrityViolationException e) {
            log.warn("Idempotency: refund for item {} in order {} already processed (possible replay). Skipping. Detail: {}",
                    event.getOrderItemId(), event.getOrderId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process OrderItemRefundRequestedEvent for item: " + event.getOrderItemId(), e);
        }
    }
}
