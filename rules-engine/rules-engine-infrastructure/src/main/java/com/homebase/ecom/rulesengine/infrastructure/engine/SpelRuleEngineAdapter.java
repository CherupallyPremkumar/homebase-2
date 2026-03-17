package com.homebase.ecom.rulesengine.infrastructure.engine;

import com.homebase.ecom.rulesengine.domain.model.Rule;
import com.homebase.ecom.rulesengine.domain.service.RuleEngine;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpelRuleEngineAdapter implements RuleEngine {
    private static final Logger log = LoggerFactory.getLogger(SpelRuleEngineAdapter.class);
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean execute(Rule rule, Map<String, Object> contextData) {
        try {
            Expression exp = parser.parseExpression(rule.getExpression());
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariables(contextData);
            Boolean result = exp.getValue(context, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            log.error("Error executing rule: " + rule.getName(), e);
            return false;
        }
    }
}
