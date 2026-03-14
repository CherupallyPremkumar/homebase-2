package com.homebase.ecom.policy.service.cmds;

import com.homebase.ecom.policy.api.dto.PolicyEventPayload;
import com.homebase.ecom.policy.domain.model.Policy;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

public abstract class AbstractPolicyAction<P extends PolicyEventPayload> extends AbstractSTMTransitionAction<Policy, P> {
    
    @Override
    public void transitionTo(Policy policy, P transitionParam, State startState, String eventId, 
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        doExecute(policy, transitionParam);
    }

    protected abstract void doExecute(Policy policy, P payload) throws Exception;
}
