package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
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
import java.util.stream.Collectors;

/**
 * Post-save hook for CHECKOUT_INITIATED state.
 * Publishes CartCheckoutInitiatedEvent to cart.events topic after transaction commits.
 */
public class CHECKOUT_INITIATEDCartPostSaveHook implements PostSaveHook<Cart> {

    private static final Logger log = LoggerFactory.getLogger(CHECKOUT_INITIATEDCartPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        if (chenilePub == null) return;

        CartCheckoutInitiatedEvent event = new CartCheckoutInitiatedEvent();
        event.setCartId(cart.getId());
        event.setUserId(cart.getCustomerId());
        event.setTimestamp(LocalDateTime.now());
        event.setTotalAmount(cart.getSubtotal().getAmount());
        event.setCurrency(cart.getCurrency());

        if (cart.getItems() != null) {
            event.setItems(cart.getItems().stream()
                    .map(item -> {
                        CartCheckoutInitiatedEvent.CartItemPayload payload = new CartCheckoutInitiatedEvent.CartItemPayload();
                        payload.setProductId(item.getProductId());
                        payload.setQuantity(item.getQuantity());
                        payload.setProductName(item.getProductName());
                        return payload;
                    })
                    .collect(Collectors.toList()));
        }

        if (!cart.getCouponCodes().isEmpty()) {
            event.setPromoCode(String.join(",", cart.getCouponCodes()));
        }
        event.setDiscountAmount(cart.getDiscountAmount().getAmount());

        publishAfterCommit(cart.getId(), event);
    }

    private void publishAfterCommit(String cartId, CartCheckoutInitiatedEvent event) {
        Runnable publishAction = () -> {
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.CART_EVENTS, body,
                        Map.of("key", cartId, "eventType", CartCheckoutInitiatedEvent.EVENT_TYPE));
                log.info("Published CHECKOUT_INITIATED event for cartId={}", cartId);
            } catch (JacksonException e) {
                log.error("Failed to serialize CartCheckoutInitiatedEvent for cartId={}", cartId, e);
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
