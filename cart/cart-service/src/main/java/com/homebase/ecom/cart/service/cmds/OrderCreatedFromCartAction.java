package com.homebase.ecom.cart.service.cmds;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.homebase.ecom.shared.Address;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.event.CartCheckoutCompletedEvent;

public class OrderCreatedFromCartAction extends AbstractSTMTransitionAction<Cart, MinimalPayload> {
    @Autowired
    private CartEventPublisher cartEventPublisher;

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        CartCheckoutCompletedEvent event = new CartCheckoutCompletedEvent();
        event.setCartId(cart.getId());
        event.setUserId(cart.getUserId());

        if (cart.getTotalAmount() != null) {
            event.setTotalAmount(cart.getTotalAmount().getAmount());
        }

        event.setTimestamp(LocalDateTime.now());

        // Currency from first item
        if (!cart.getItems().isEmpty() && cart.getItems().get(0).getPrice() != null) {
            event.setCurrency(cart.getItems().get(0).getPrice().getCurrency());
        }

        event.setItems(cart.getItems().stream()
                .map((CartItem item) -> {
                    CartCheckoutCompletedEvent.CartItemPayload info = new CartCheckoutCompletedEvent.CartItemPayload();
                    info.setProductId(item.getProductId());
                    info.setQuantity(item.getQuantity());
                    if (item.getPrice() != null && item.getPrice().getAmount() != null) {
                        info.setUnitPrice(item.getPrice().getAmount());
                    }
                    info.setSupplierId(item.getSellerId());
                    return info;
                })
                .collect(Collectors.toList()));

        // Transfer address info from cart to event
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        if (cart.getShippingAddress() != null) {
            try {
                event.setShippingAddress(
                        mapper.readValue(cart.getShippingAddress(), Address.class));
            } catch (Exception e) {
                // Log error but continue
            }
        }
        if (cart.getBillingAddress() != null) {
            try {
                event.setBillingAddress(
                        mapper.readValue(cart.getBillingAddress(), Address.class));
            } catch (Exception e) {
                // Log error but continue
            }
        }

        // Transfer promo info
        event.setPromoCode(cart.getAppliedPromoCode());
        if (cart.getDiscountAmount() != null) {
            event.setDiscountAmount(cart.getDiscountAmount().getAmount());
        }

        cartEventPublisher.publishCartCheckoutCompleted(event);
    }
}
