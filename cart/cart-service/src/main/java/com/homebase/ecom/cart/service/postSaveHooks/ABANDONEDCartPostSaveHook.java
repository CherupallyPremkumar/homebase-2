package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartAbandonedEvent;
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
 * Post-save hook for ABANDONED state.
 * Publishes CartAbandonedEvent after transaction commits — triggers inventory release.
 */
public class ABANDONEDCartPostSaveHook implements PostSaveHook<Cart> {

    private static final Logger log = LoggerFactory.getLogger(ABANDONEDCartPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        if (chenilePub == null) return;

        CartAbandonedEvent event = new CartAbandonedEvent(cart.getId(), LocalDateTime.now());

        publishAfterCommit(cart.getId(), event);
    }

    private void publishAfterCommit(String cartId, CartAbandonedEvent event) {
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.CART_EVENTS, body,
                        Map.of("key", cartId, "eventType", CartAbandonedEvent.EVENT_TYPE));
                log.info("Published CART_ABANDONED event for cartId={}", cartId);
            } catch (JacksonException e) {
                log.error("Failed to serialize CartAbandonedEvent for cartId={}", cartId, e);
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
