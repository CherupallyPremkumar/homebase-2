package com.homebase.ecom.compliance.service.action;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.compliance.model.Agreement;

/**
 * Default transition action invoked when no specific transition action is specified for agreements.
 */
public class DefaultAgreementTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<Agreement, PayloadType> {
    @Override
    public void transitionTo(Agreement agreement, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {
    }
}
