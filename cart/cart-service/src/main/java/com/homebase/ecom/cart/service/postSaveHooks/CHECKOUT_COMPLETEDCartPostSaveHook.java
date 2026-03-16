package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartCheckoutCompletedEvent;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Post-save hook for CHECKOUT_COMPLETED state.
 * Publishes CartCheckoutCompletedEvent after transaction commits.
 */
public class CHECKOUT_COMPLETEDCartPostSaveHook implements PostSaveHook<Cart> {

    private static final Logger log = LoggerFactory.getLogger(CHECKOUT_COMPLETEDCartPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        if (chenilePub == null) return;

        CartCheckoutCompletedEvent event = new CartCheckoutCompletedEvent();
        event.setCartId(cart.getId());
        event.setUserId(cart.getCustomerId());
        event.setTotalAmount(cart.getSubtotal().getAmount());
        event.setCurrency(cart.getCurrency());
        event.setTimestamp(LocalDateTime.now());

        publishAfterCommit(cart.getId(), event);
    }

    private void publishAfterCommit(String cartId, CartCheckoutCompletedEvent event) {
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.CART_EVENTS, body,
                        Map.of("key", cartId, "eventType", CartCheckoutCompletedEvent.EVENT_TYPE));
                log.info("Published CHECKOUT_COMPLETED event for cartId={}", cartId);
            } catch (JacksonException e) {
                log.error("Failed to serialize CartCheckoutCompletedEvent for cartId={}", cartId, e);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
        } else {
            publishAction.run();
        }
    }
}
