package com.homebase.ecom.disputes.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.disputes.model.Dispute;

/**
    This class is invoked if no specific transition action is specified
    Extend this class to do generic things that are relevant for all actions in the workflow
*/

public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<Dispute, PayloadType> {
    @Override
    public void transitionTo(Dispute dispute, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {

    }
}
