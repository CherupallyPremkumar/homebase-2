package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRuleSetAction<P extends RuleSetEventPayload> extends AbstractSTMTransitionAction<RuleSet, P> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final RulesEngineValidator validator;

    protected AbstractRuleSetAction(RulesEngineValidator validator) {
        this.validator = validator;
    }

    @Override
    public void transitionTo(RuleSet ruleSet, P transitionParam, State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        doExecute(ruleSet, transitionParam, startState, eventId);
    }

    protected abstract void doExecute(RuleSet ruleSet, P payload, State startState, String eventId) throws Exception;
}
