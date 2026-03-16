package com.homebase.ecom.payment.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
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
 * PostSaveHook for ABANDONED state.
 * Publishes PAYMENT_FAILED (terminal failure) to payment.events topic.
 */
public class ABANDONEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(ABANDONEDPaymentPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        if (chenilePub == null) return;

        PaymentFailedEvent event = new PaymentFailedEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(),
                "Payment abandoned after " + payment.getRetryCount() + " retries", LocalDateTime.now());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.PAYMENT_EVENTS, body,
                    Map.of("key", payment.getOrderId(), "eventType", PaymentFailedEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize PaymentFailedEvent (abandoned) for orderId={}", payment.getOrderId(), e);
            return;
        }
        log.warn("Published PAYMENT_FAILED (abandoned) for orderId={}, retryCount={}",
                payment.getOrderId(), payment.getRetryCount());
    }
}
