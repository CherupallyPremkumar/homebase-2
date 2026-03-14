package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.MoveToWishlistPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

public class MoveToWishlistAction extends AbstractCartAction<MoveToWishlistPayload> {

    @Override
    public void transitionTo(Cart cart, MoveToWishlistPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, org.chenile.stm.model.Transition transition)
            throws Exception {
        validateCustomerAccess();
        logActivity(cart, "Move To Wishlist", "Moved product " + payload.getProductId() + " to wishlist.");
    }
}
