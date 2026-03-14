package com.homebase.ecom.catalog.domain.service;

import com.homebase.ecom.product.dto.ProductCatalogDetails;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Intelligence Layer: Evaluates dynamic rules for Collections.
 * Uses SpEL (Spring Expression Language) to match products against string
 * expressions.
 * Example Rule: "price.amount < 50 and active == true"
 */
@Component
public class DynamicRuleEvaluator {

    private static final Logger log = LoggerFactory.getLogger(DynamicRuleEvaluator.class);
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * Evaluate a rule expression against a product.
     * 
     * @param ruleExpression the SpEL expression (e.g. "price.amount < 50")
     * @param product        the product data to evaluate
     * @return true if the product matches the rule, false otherwise or on error
     */
    public boolean matches(String ruleExpression, ProductCatalogDetails product) {
        if (ruleExpression == null || ruleExpression.isBlank()) {
            return false;
        }
        try {
            StandardEvaluationContext context = new StandardEvaluationContext(product);
            // Best Practice: We could restrict the context here for security if rules were user-generated
            // context.setBeanResolver(null); // Disable bean references

            Boolean result = parser.parseExpression(ruleExpression).getValue(context, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            // Log as warning, don't crash the batch job
            log.warn("Invalid rule evaluation for product {}: Rule='{}' Error={}", 
                    product.getProductId(), ruleExpression, e.getMessage());
            return false;
        }
    }
}
