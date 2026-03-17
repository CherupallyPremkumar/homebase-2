package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Post-save hook for CHECKOUT_INITIATED state.
 * Publishes CartCheckoutInitiatedEvent to cart.events topic after transaction commits.
 */
public class CHECKOUT_INITIATEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public CHECKOUT_INITIATEDCartPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        super(chenilePub, objectMapper);
    }

    @Override
    protected Object buildEvent(State startState, State endState, Cart cart, TransientMap map) {
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

        return event;
    }

    @Override
    protected String eventType() {
        return CartCheckoutInitiatedEvent.EVENT_TYPE;
    }
}
