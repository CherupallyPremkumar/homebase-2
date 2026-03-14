package com.homebase.ecom.policy.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.policy.domain.model.Policy;

/**
    This class is invoked if no specific transition action is specified
    Extend this class to do generic things that are relevant for all actions in the workflow
*/

public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<Policy, PayloadType> {
    @Override
    public void transitionTo(Policy policy, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {

    }
}
