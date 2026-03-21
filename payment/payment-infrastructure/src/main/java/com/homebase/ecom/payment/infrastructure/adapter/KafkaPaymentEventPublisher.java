package com.homebase.ecom.payment.infrastructure.adapter;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.PaymentFailedEvent;
import com.homebase.ecom.shared.event.PaymentRefundedEvent;
import com.homebase.ecom.shared.event.PaymentSucceededEvent;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Kafka-based infrastructure adapter for {@link PaymentEventPublisherPort}.
 * Uses ChenilePub for Kafka publishing with Chenile header propagation.
 * Handles event construction, serialization, and error handling.
 */
public class KafkaPaymentEventPublisher implements PaymentEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaPaymentEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaPaymentEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishPaymentSucceeded(Payment payment) {
        PaymentSucceededEvent event = new PaymentSucceededEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(), LocalDateTime.now());
        publishEvent(event, payment.getOrderId(), PaymentSucceededEvent.EVENT_TYPE,
                "PaymentSucceededEvent");
        log.info("Published PAYMENT_SUCCEEDED for orderId={}, paymentId={}",
                payment.getOrderId(), payment.getId());
    }

    @Override
    public void publishPaymentFailed(Payment payment) {
        PaymentFailedEvent event = new PaymentFailedEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(),
                payment.getFailureReason(), LocalDateTime.now());
        publishEvent(event, payment.getOrderId(), PaymentFailedEvent.EVENT_TYPE,
                "PaymentFailedEvent");
        log.warn("Published PAYMENT_FAILED for orderId={}, reason={}",
                payment.getOrderId(), payment.getFailureReason());
    }

    @Override
    public void publishPaymentAbandoned(Payment payment) {
        PaymentFailedEvent event = new PaymentFailedEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(),
                "Payment abandoned after " + payment.getRetryCount() + " retries",
                LocalDateTime.now());
        publishEvent(event, payment.getOrderId(), PaymentFailedEvent.EVENT_TYPE,
                "PaymentFailedEvent (abandoned)");
        log.warn("Published PAYMENT_FAILED (abandoned) for orderId={}, retryCount={}",
                payment.getOrderId(), payment.getRetryCount());
    }

    @Override
    public void publishPaymentRefunded(Payment payment) {
        PaymentRefundedEvent event = new PaymentRefundedEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(),
                payment.getAmount(), "Refund completed", LocalDateTime.now());
        publishEvent(event, payment.getOrderId(), PaymentRefundedEvent.EVENT_TYPE,
                "PaymentRefundedEvent");
        log.info("Published PAYMENT_REFUNDED for orderId={}, amount={}",
                payment.getOrderId(), payment.getAmount());
    }

    private void publishEvent(Object event, String key, String eventType, String eventName) {
        if (chenilePub == null) {
            log.debug("ChenilePub not available, skipping {} publish", eventName);
            return;
        }
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.PAYMENT_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for orderId={}", eventName, key, e);
        }
    }
}
