package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;

public abstract class AbstractRuleSetAction<P extends RuleSetEventPayload> extends AbstractSTMTransitionAction<RuleSet, P> {

    @Override
    public void transitionTo(RuleSet ruleSet, P transitionParam, State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        doExecute(ruleSet, transitionParam);
    }

    protected abstract void doExecute(RuleSet ruleSet, P payload) throws Exception;
}
