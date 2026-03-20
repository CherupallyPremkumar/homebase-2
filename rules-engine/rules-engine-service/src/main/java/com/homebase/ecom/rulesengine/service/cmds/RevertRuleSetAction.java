package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;

/**
 * REVIEW → DRAFT: Reverts the RuleSet back to draft. Comment required.
 */
public class RevertRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {

    public RevertRuleSetAction(RulesEngineValidator validator) {
        super(validator);
    }

    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload, State startState, String eventId) throws Exception {
        String comments = (payload != null) ? payload.comments : null;
        validator.validateCommentRequired(comments, "revert");
        log.info("RuleSet '{}' reverted to DRAFT: {}", ruleSet.getName(), comments);
    }
}
