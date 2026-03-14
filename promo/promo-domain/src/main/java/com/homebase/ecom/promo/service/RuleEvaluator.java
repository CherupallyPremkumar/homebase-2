package com.homebase.ecom.promo.service;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.Promotion;
import com.homebase.ecom.promo.model.PromoContext;
import com.homebase.ecom.promo.model.PromotionResult;
import com.homebase.ecom.promo.rule.PromotionRule;
import com.homebase.ecom.shared.Money;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class RuleEvaluator {

    private static final Logger log = LoggerFactory.getLogger(RuleEvaluator.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void evaluateAll(PromoContext context) {
        CartSnapshot cart = context.getCart();
        List<Promotion> promotions = context.getEligiblePromotions();

        for (Promotion promotion : promotions) {
            PromotionResult result = evaluatePromotion(promotion, cart);
            context.addEvaluationResult(result);
        }
    }

    public PromotionResult evaluatePromotion(Promotion promotion, CartSnapshot cart) {
        if (!promotion.isApplicable()) {
            return PromotionResult.notApplicable(promotion);
        }

        // In a full implementation, we'd parse the 'strategy' JSON and use a
        // StrategyFactory
        // For now, let's assume a match if it's within valid date range (already
        // checked in isApplicable)

        // return new PromotionResult(promotion, new Money(BigDecimal.ZERO,
        // cart.getTotalAmount().getCurrency()), true);

        // Placeholder implementation for logic that will be refined in Phase 9
        boolean ruleSatisfied = evaluateRule(promotion, cart);
        if (ruleSatisfied) {
            return new PromotionResult(promotion,
                    new Money(BigDecimal.valueOf(10.0), cart.getTotalAmount().getCurrency()), true);
        } else {
            return PromotionResult.notSatisfied(promotion);
        }
    }

    // This method encapsulates the original rule evaluation logic
    public boolean evaluateRule(Promotion promotion, CartSnapshot cart) {
        if (!promotion.isApplicable())
            return false;

        try {
            // Desk-serialization of rules from JSON to PromotionRule object
            // This assumes rules are stored as JSON in the database
            PromotionRule rule = objectMapper.readValue(promotion.getRules(), PromotionRule.class);
            return rule.matchesConditions(cart);
        } catch (Exception e) {
            log.error("Error evaluating promotion rule for promotion ID {}: {}", promotion.getId(), e.getMessage());
            return false;
        }
    }
}
