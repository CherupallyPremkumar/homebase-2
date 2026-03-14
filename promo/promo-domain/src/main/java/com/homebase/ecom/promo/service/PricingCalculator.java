package com.homebase.ecom.promo.service;

import com.homebase.ecom.promo.model.PromoContext;
import com.homebase.ecom.promo.model.ResolutionStrategy;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PricingCalculator {

    private static final Logger log = LoggerFactory.getLogger(PricingCalculator.class);

    private final RuleEvaluator ruleEvaluator;
    private final ConflictResolver conflictResolver;

    public PricingCalculator(RuleEvaluator ruleEvaluator, ConflictResolver conflictResolver) {
        this.ruleEvaluator = ruleEvaluator;
        this.conflictResolver = conflictResolver;
    }

    public void calculate(PromoContext context) {
        log.debug("Starting pricing calculation for cart: {}", context.getCart());

        // 1. Evaluate all eligible promotions
        ruleEvaluator.evaluateAll(context);

        // 2. Resolve conflicts and select best deal (defaulting to BEST_DEAL strategy for now)
        conflictResolver.resolve(context, ResolutionStrategy.BEST_DEAL);

        log.debug("Pricing calculation completed. Best deal: {}", context.getBestDeal());
    }
}
