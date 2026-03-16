package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartMergedEvent;
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
 * Post-save hook for MERGED state.
 * Publishes CartMergedEvent after transaction commits — triggers inventory release
 * for the source (guest) cart's reservations.
 */
public class MERGEDCartPostSaveHook implements PostSaveHook<Cart> {

    private static final Logger log = LoggerFactory.getLogger(MERGEDCartPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        if (chenilePub == null) return;

        String targetCartId = map != null ? (String) map.get("targetCartId") : null;

        CartMergedEvent event = new CartMergedEvent(
                cart.getId(),
                targetCartId,
                cart.getCustomerId(),
                LocalDateTime.now());

        publishAfterCommit(cart.getId(), event);
    }

    private void publishAfterCommit(String cartId, CartMergedEvent event) {
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.CART_EVENTS, body,
                        Map.of("key", cartId, "eventType", CartMergedEvent.EVENT_TYPE));
                log.info("Published CART_MERGED event for sourceCartId={} targetCartId={}",
                        event.getSourceCartId(), event.getTargetCartId());
            } catch (JacksonException e) {
                log.error("Failed to serialize CartMergedEvent for cartId={}", cartId, e);
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
