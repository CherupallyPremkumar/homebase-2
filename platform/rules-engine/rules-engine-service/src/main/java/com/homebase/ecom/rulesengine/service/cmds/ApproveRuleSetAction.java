package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;

/**
 * REVIEW → ACTIVE: Approves the RuleSet and marks it active.
 * The RuleSet was already validated during submit; this action activates it.
 */
public class ApproveRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {

    public ApproveRuleSetAction(RulesEngineValidator validator) {
        super(validator);
    }

    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload, State startState, String eventId) throws Exception {
        ruleSet.setActive(true);
        log.info("RuleSet '{}' approved and activated", ruleSet.getName());
    }
}
