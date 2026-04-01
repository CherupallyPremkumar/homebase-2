package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;

/**
 * ACTIVE → DEPRECATED: Deprecates the RuleSet. Comment required.
 * Rules are no longer enforced after deprecation.
 */
public class DeprecateRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {

    public DeprecateRuleSetAction(RulesEngineValidator validator) {
        super(validator);
    }

    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload, State startState, String eventId) throws Exception {
        String comments = (payload != null) ? payload.comments : null;
        validator.validateCommentRequired(comments, "deprecate");
        ruleSet.setActive(false);
        log.info("RuleSet '{}' deprecated: {}", ruleSet.getName(), comments);
    }
}
