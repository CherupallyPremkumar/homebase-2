package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.FlagUnavailableCartPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Flags cart items as unavailable when stock is depleted or product is discontinued.
 *
 * If variantId is set, flags that specific variant.
 * If only productId is set, flags all variants of that product.
 * Recalculates pricing after flagging.
 */
public class FlagUnavailableCartAction extends AbstractCartAction<FlagUnavailableCartPayload> {

    @Override
    public void transitionTo(Cart cart, FlagUnavailableCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        boolean flagged = false;

        if (payload.variantId != null) {
            for (CartItem item : cart.getItems()) {
                if (payload.variantId.equals(item.getVariantId())) {
                    item.setAvailable(false);
                    flagged = true;
                    logActivity(cart, "flagUnavailable", "Variant " + payload.variantId + " marked unavailable");
                }
            }
        } else if (payload.productId != null) {
            for (CartItem item : cart.getItems()) {
                if (payload.productId.equals(item.getProductId())) {
                    item.setAvailable(false);
                    flagged = true;
                }
            }
            if (flagged) {
                logActivity(cart, "flagUnavailable", "All variants of product " + payload.productId + " marked unavailable");
            }
        }

        if (flagged) {
            recalculatePricing(cart);
        }
    }
}
