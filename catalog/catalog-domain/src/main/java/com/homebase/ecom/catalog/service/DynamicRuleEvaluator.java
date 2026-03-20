package com.homebase.ecom.catalog.service;

import com.homebase.ecom.catalog.model.CatalogItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Domain service that evaluates SpEL-like rule expressions against CatalogItems.
 * Used by dynamic collections to determine membership.
 */
public class DynamicRuleEvaluator {

    private static final Logger log = LoggerFactory.getLogger(DynamicRuleEvaluator.class);

    public boolean matches(String ruleExpression, CatalogItem item) {
        if (ruleExpression == null || ruleExpression.isBlank()) {
            return false;
        }
        try {
            org.springframework.expression.spel.standard.SpelExpressionParser parser =
                    new org.springframework.expression.spel.standard.SpelExpressionParser();
            org.springframework.expression.Expression expression = parser.parseExpression(ruleExpression);

            org.springframework.expression.spel.support.StandardEvaluationContext ctx =
                    new org.springframework.expression.spel.support.StandardEvaluationContext(item);

            Boolean result = expression.getValue(ctx, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.warn("Rule evaluation failed for expression '{}': {}", ruleExpression, e.getMessage());
            return false;
        }
    }
}
