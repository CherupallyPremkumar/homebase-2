package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default transition action invoked when no specific transition action is specified.
 * Extend this class to add generic behavior relevant for all return processing transitions.
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<ReturnProcessingSaga, PayloadType> {

    @Override
    public void transitionTo(ReturnProcessingSaga saga, PayloadType payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) {
        // Default no-op; specific actions override this
    }
}
