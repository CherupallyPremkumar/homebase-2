package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * Base class for Cart post-save hooks that publish Kafka events.
 * Handles serialization and publish-after-commit logic.
 * Subclasses only need to implement buildEvent() and eventType().
 */
public abstract class AbstractCartEventPostSaveHook implements PostSaveHook<Cart> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final ChenilePub chenilePub;
    protected final ObjectMapper objectMapper;

    protected AbstractCartEventPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    protected abstract Object buildEvent(State startState, State endState, Cart cart, TransientMap map);

    protected abstract String eventType();

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        Object event = buildEvent(startState, endState, cart, map);
        if (event == null) return;
        publishAfterCommit(cart.getId(), event);
    }

    private void publishAfterCommit(String cartId, Object event) {
        String evtType = eventType();
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.asyncPublish(KafkaTopics.CART_EVENTS, body,
                        Map.of("key", cartId, "eventType", evtType));
                log.info("Published {} event for cartId={}", evtType, cartId);
            } catch (JacksonException e) {
                log.error("Failed to serialize {} for cartId={}", evtType, cartId, e);
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
