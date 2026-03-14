package com.homebase.ecom.product.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.product.domain.model.Product;

/**
 * This class is invoked if no specific transition action is specified.
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<Product, PayloadType> {
    @Override
    public void transitionTo(Product product, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {
        // Default implementation logic here
    }
}