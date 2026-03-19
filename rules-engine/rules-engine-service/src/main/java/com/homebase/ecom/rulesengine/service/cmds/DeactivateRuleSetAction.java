package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;

/**
 * ACTIVE → DRAFT: Deactivates the RuleSet and sends it back to draft. Comment required.
 */
public class DeactivateRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {

    public DeactivateRuleSetAction(RulesEngineValidator validator) {
        super(validator);
    }

    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload, State startState, String eventId) throws Exception {
        String comments = (payload != null) ? payload.comments : null;
        validator.validateCommentRequired(comments, "deactivate");
        ruleSet.setActive(false);
        log.info("RuleSet '{}' deactivated back to DRAFT: {}", ruleSet.getName(), comments);
    }
}
