package com.homebase.ecom.rulesengine.service.cmds;

import com.homebase.ecom.rulesengine.api.dto.RuleSetEventPayload;
import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.domain.service.RulesEngineValidator;
import org.chenile.stm.State;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * DRAFT → REVIEW: Validates the RuleSet is complete and all rules have valid SpEL expressions.
 */
public class SubmitRuleSetAction extends AbstractRuleSetAction<RuleSetEventPayload> {
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public SubmitRuleSetAction(RulesEngineValidator validator) {
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
        log.info("RuleSet '{}' submitted for review", ruleSet.getName());
    }
}
