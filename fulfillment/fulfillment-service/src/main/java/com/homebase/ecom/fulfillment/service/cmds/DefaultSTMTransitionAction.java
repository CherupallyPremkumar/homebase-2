package com.homebase.ecom.fulfillment.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

/**
 * Default transition action invoked when no specific transition action is specified.
 * Extend this class to add generic logic relevant for all fulfillment saga transitions.
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<FulfillmentSaga, PayloadType> {
    @Override
    public void transitionTo(FulfillmentSaga saga, PayloadType payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm,
                             Transition transition) {
    }
}
