package com.homebase.ecom.onboarding.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.onboarding.model.OnboardingSaga;

/**
 * Default transition action invoked when no specific action is specified.
 */
public class DefaultOnboardingSTMTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<OnboardingSaga, PayloadType> {
    @Override
    public void transitionTo(OnboardingSaga saga, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {
    }
}
