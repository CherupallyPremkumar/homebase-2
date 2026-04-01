package com.homebase.ecom.promo.service;

import com.homebase.ecom.promo.model.PromoContext;
import com.homebase.ecom.promo.model.PromotionResult;
import com.homebase.ecom.promo.model.ResolutionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConflictResolver {

    private static final Logger log = LoggerFactory.getLogger(ConflictResolver.class);

    public void resolve(PromoContext context, ResolutionStrategy strategy) {
        List<PromotionResult> results = context.getApplicableResults();
        if (results.isEmpty()) return;

        switch (strategy) {
            case BEST_DEAL:
                resolveBestDeal(context, results);
                break;
            case STACK_ALL:
                resolveStackAll(context, results);
                break;
            default:
                log.warn("Resolution strategy {} not yet fully implemented, falling back to BEST_DEAL", strategy);
                resolveBestDeal(context, results);
        }
    }

    private void resolveBestDeal(PromoContext context, List<PromotionResult> results) {
        PromotionResult best = results.stream()
                .max(Comparator.comparing(r -> r.getSavings().getAmount()))
                .orElse(null);
        context.setBestDeal(best);
    }

    private void resolveStackAll(PromoContext context, List<PromotionResult> results) {
        // Simple stacking logic: apply all that are stackable
        // If an exclusive one exists and is better than the sum of stackables, pick it?
        // For now, let's just pick the best as the 'bestDeal' but keep track of all
        resolveBestDeal(context, results);
    }
}
