package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * DRAFT → ACTIVE (direct) or DEPRECATED → ACTIVE (reactivate).
 * Validates everything and marks active. Used when skipping the REVIEW step.
 */
public class ActivateRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public ActivateRuleSetAction(RulesEngineValidator validator) {
        super(validator);
    }

    @Override
    protected void doExecute(RuleSet ruleSet, RuleSetEventPayload payload, State startState, String eventId) throws Exception {
        validator.validateForSubmit(ruleSet);
        for (Rule rule : ruleSet.getRules()) {
            try {
                expressionParser.parseExpression(rule.getExpression());
            } catch (Exception e) {
                validator.validateExpressionParseable(rule.getName(), e.getMessage());
            }
        }
        ruleSet.setActive(true);
        log.info("RuleSet '{}' activated (from {})", ruleSet.getName(), startState);
    }
}
