package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.time.LocalDateTime;

/**
 * STM transition action for reactivate event.
 * Reactivates an abandoned cart — resets expiration timer.
 */
public class ReactivateCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Reset expiration
        int expirationHours = cartPolicyValidator.getCartExpirationHours();
        cart.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));

        logActivity(cart, "reactivate", "Cart reactivated from " + startState.getStateId());
    }
}
