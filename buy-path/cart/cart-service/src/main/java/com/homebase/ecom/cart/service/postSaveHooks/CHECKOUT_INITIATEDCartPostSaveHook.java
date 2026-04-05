package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.event.CartCheckoutInitiatedEvent;
import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.event.CartItemPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Post-save hook for CHECKOUT_INITIATED state.
 * Publishes cart checkout initiated event to trigger checkout saga.
 */
public class CHECKOUT_INITIATEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public CHECKOUT_INITIATEDCartPostSaveHook(CartEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CartEvent buildEvent(Cart cart, TransientMap map) {
        List<CartItemPayload> items = cart.getItems() != null
                ? cart.getItems().stream()
                    .map(item -> new CartItemPayload(
                            item.getProductId(),
                            item.getSku(),
                            item.getProductName(),
                            item.getQuantity(),
                            item.getUnitPrice().getAmount(),
                            item.getSupplierId()))
                    .collect(Collectors.toList())
                : List.of();

        String promoCode = cart.getCouponCodes().isEmpty()
                ? null
                : String.join(",", cart.getCouponCodes());

        return new CartCheckoutInitiatedEvent(
                cart.getId(),
                cart.getCustomerId(),
                cart.getSubtotal().getAmount(),
                cart.getCurrency(),
                items,
                promoCode,
                cart.getDiscountAmount().getAmount(),
                LocalDateTime.now());
    }
}
