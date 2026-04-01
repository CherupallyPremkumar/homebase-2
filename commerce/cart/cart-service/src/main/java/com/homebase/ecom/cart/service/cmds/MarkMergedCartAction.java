package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

/**
 * STM transition action for markMerged event (SYSTEM-triggered).
 * Called internally by MergeCartAction on the source (guest) cart
 * to transition it to MERGED terminal state.
 */
public class MarkMergedCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        logActivity(cart, "markMerged", "Source cart merged into target, transitioning to MERGED");
    }
}
