package com.homebase.ecom.payment.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.PaymentSucceededEvent;
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
 * PostSaveHook for SUCCEEDED state.
 * Publishes PAYMENT_SUCCEEDED to payment.events topic for order service consumption.
 */
public class SUCCEEDEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(SUCCEEDEDPaymentPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        if (chenilePub == null) return;

        PaymentSucceededEvent event = new PaymentSucceededEvent(
                payment.getOrderId(), payment.getGatewayTransactionId(), LocalDateTime.now());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.PAYMENT_EVENTS, body,
                    Map.of("key", payment.getOrderId(), "eventType", PaymentSucceededEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize PaymentSucceededEvent for orderId={}", payment.getOrderId(), e);
            return;
        }
        log.info("Published PAYMENT_SUCCEEDED for orderId={}, paymentId={}", payment.getOrderId(), payment.getId());
    }
}
