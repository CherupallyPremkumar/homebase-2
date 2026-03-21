package com.homebase.ecom.demonotification.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.StateEntity;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default no-op transition action for demo notification STM.
 */
public class DemoDefaultTransitionAction<T extends StateEntity>
        extends AbstractSTMTransitionAction<T, MinimalPayload> {

    @Override
    public void transitionTo(T entity, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) {
        // no-op -- state transition is handled by the STM engine
    }
}
