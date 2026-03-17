package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;

public class SubmitRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {
    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload) throws Exception {
        System.out.println("Submitting rule set: " + ruleSet.getId());
    }
}
