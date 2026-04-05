package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.time.LocalDateTime;

/**
 * STM transition action for cancelCheckout event.
 * Resets cart expiration back to standard TTL and returns cart to ACTIVE.
 */
public class CancelCheckoutCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Reset expiration from checkout reservation (15 min) back to standard cart TTL
        int expirationHours = cartPolicyValidator.getCartExpirationHours();
        cart.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));

        logActivity(cart, "cancelCheckout", "Checkout cancelled, cart returned to ACTIVE, expiry reset to " + expirationHours + "h");
    }
}
