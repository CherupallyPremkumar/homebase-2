package com.homebase.ecom.compliance.action;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.PlatformPolicy;

/**
 * Default transition action invoked when no specific transition action is specified for platform policies.
 */
public class DefaultPlatformPolicyTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<PlatformPolicy, PayloadType> {
    @Override
    public void transitionTo(PlatformPolicy policy, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {
    }
}
