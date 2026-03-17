package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;

public class ApproveRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {
    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload) throws Exception {
        System.out.println("Approving rule set: " + ruleSet.getId());
    }
}
