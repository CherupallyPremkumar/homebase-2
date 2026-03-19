package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

/**
 * Default (fallback) transition action. All transitions have explicit actions,
 * so this is a no-op safety net. Create validation is handled by RuleSetEntryAction.
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<RuleSet, PayloadType> {

    @Override
    public void transitionTo(RuleSet ruleSet, PayloadType payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) {
        // No-op — all transitions have explicit actions
    }
}
