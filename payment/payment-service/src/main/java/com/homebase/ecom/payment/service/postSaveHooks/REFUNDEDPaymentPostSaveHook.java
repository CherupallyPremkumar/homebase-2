package com.homebase.ecom.payment.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.PaymentRefundedEvent;
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
 * PostSaveHook for REFUNDED state.
 * Publishes PAYMENT_REFUNDED to payment.events for order and settlement service consumption.
 * Also triggers notification to customer.
 */
public class REFUNDEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(REFUNDEDPaymentPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        // Publish to Kafka for order + settlement services
        if (chenilePub != null) {
            PaymentRefundedEvent event = new PaymentRefundedEvent(
                    payment.getOrderId(), payment.getGatewayTransactionId(),
                    payment.getAmount(), "Refund completed", LocalDateTime.now());
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.PAYMENT_EVENTS, body,
                        Map.of("key", payment.getOrderId(), "eventType", PaymentRefundedEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize PaymentRefundedEvent for orderId={}", payment.getOrderId(), e);
            }
            log.info("Published PAYMENT_REFUNDED for orderId={}, amount={}", payment.getOrderId(), payment.getAmount());
        }

        // Notify customer
        if (notificationPort != null) {
            try {
                notificationPort.notifyRefundCompleted(payment);
            } catch (Exception e) {
                log.error("Failed to send refund notification for orderId={}", payment.getOrderId(), e);
            }
        }
    }
}
