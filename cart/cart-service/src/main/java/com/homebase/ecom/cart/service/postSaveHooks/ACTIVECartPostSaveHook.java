package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for ACTIVE state.
 * No-op currently — placeholder for future analytics (e.g., cart modification events).
 */
public class ACTIVECartPostSaveHook implements PostSaveHook<Cart> {

    private static final Logger log = LoggerFactory.getLogger(ACTIVECartPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        log.debug("Cart {} transitioned to ACTIVE from {}", cart.getId(),
                startState != null ? startState.getStateId() : "initial");
    }
}
