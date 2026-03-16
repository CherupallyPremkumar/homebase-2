package com.homebase.ecom.payment.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.PaymentFailedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * PostSaveHook for FAILED state.
 * Publishes PAYMENT_FAILED to payment.events topic for order service consumption.
 * Also triggers notification to customer.
 */
public class FAILEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDPaymentPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        // Publish to Kafka
        if (chenilePub != null) {
            PaymentFailedEvent event = new PaymentFailedEvent(
                    payment.getOrderId(), payment.getGatewayTransactionId(),
                    payment.getFailureReason(), LocalDateTime.now());
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.PAYMENT_EVENTS, body,
                        Map.of("key", payment.getOrderId(), "eventType", PaymentFailedEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize PaymentFailedEvent for orderId={}", payment.getOrderId(), e);
            }
            log.warn("Published PAYMENT_FAILED for orderId={}, reason={}", payment.getOrderId(), payment.getFailureReason());
        }

        // Notify customer
        if (notificationPort != null) {
            try {
                notificationPort.notifyPaymentFailed(payment);
            } catch (Exception e) {
                log.error("Failed to send payment failure notification for orderId={}", payment.getOrderId(), e);
            }
        }
    }
}
