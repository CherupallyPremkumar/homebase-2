package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.Address;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Contains customized post Save Hook for the State ID.
 */
public class CHECKOUT_IN_PROGRESSCartPostSaveHook implements PostSaveHook<Cart> {

    @Autowired
    private CartEventPublisher cartEventPublisher;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        CartCheckoutInitiatedEvent event = new CartCheckoutInitiatedEvent();
        event.setCartId(cart.getId());
        event.setUserId(cart.getUserId());
        event.setTimestamp(LocalDateTime.now());

        if (cart.getTotalAmount() != null) {
            event.setTotalAmount(cart.getTotalAmount().getAmount());
        }

        // Currency from first item
        if (cart.getItems() != null && !cart.getItems().isEmpty() && cart.getItems().get(0).getPrice() != null) {
            event.setCurrency(cart.getItems().get(0).getPrice().getCurrency());
        }

        if (cart.getItems() != null) {
            event.setItems(cart.getItems().stream()
                    .map(item -> {
                        CartCheckoutInitiatedEvent.CartItemPayload payload = new CartCheckoutInitiatedEvent.CartItemPayload();
                        payload.setProductId(item.getProductId());
                        payload.setQuantity(item.getQuantity());
                        if (item.getPrice() != null) {
                            payload.setUnitPrice(item.getPrice());
                        }
                        payload.setSupplierId(item.getSellerId());
                        return payload;
                    })
                    .collect(Collectors.toList()));
        }

        // Addresses
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        if (cart.getShippingAddress() != null) {
            try {
                event.setShippingAddress(
                        mapper.readValue(cart.getShippingAddress(), Address.class));
            } catch (Exception e) {
                // Ignore parsing errors for now
            }
        }
        if (cart.getBillingAddress() != null) {
            try {
                event.setBillingAddress(
                        mapper.readValue(cart.getBillingAddress(), Address.class));
            } catch (Exception e) {
                // Ignore
            }
        }

        event.setPromoCode(cart.getAppliedPromoCode());
        if (cart.getDiscountAmount() != null) {
            event.setDiscountAmount(cart.getDiscountAmount().getAmount());
        }

        cartEventPublisher.publishCartCheckoutInitiated(event);
    }
}
