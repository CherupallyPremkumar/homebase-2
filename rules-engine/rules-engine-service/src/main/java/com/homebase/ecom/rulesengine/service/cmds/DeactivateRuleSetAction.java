package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;

public class DeactivateRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {
    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload) throws Exception {
        ruleSet.setActive(false);
        System.out.println("Deactivating rule set: " + ruleSet.getId());
    }
}
