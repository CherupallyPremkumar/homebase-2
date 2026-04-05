package com.homebase.ecom.promo.rule;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.Promotion;
import com.homebase.ecom.promo.model.PromotionResult;
import com.homebase.ecom.shared.Money;

import java.util.Map;

public class JsonPromotionRule extends PromotionRule {

    private Map<String, Object> jsonConfig;
    private final ConditionEvaluator conditionEvaluator = new ConditionEvaluator();

    public Map<String, Object> getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(Map<String, Object> jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    @Override
    public PromotionResult evaluate(CartSnapshot cart) {
        if (!isApplicable() || !matchesConditions(cart)) {
            return PromotionResult.notApplicable(null); // Need context to provide the right Promotion entity
        }

        Money savings = calculateSavings(cart);
        return new PromotionResult(null, savings, true);
    }

    @Override
    public boolean matchesConditions(CartSnapshot cart) {
        if (conditions == null || conditions.isEmpty()) return true;
        return conditions.stream().allMatch(c -> conditionEvaluator.evaluate(c, cart));
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        // This is a simplified implementation.
        // Real implementation would look at 'actions' and apply them.
        if (actions == null || actions.isEmpty()) {
            return Money.zero(cart.getTotalAmount().getCurrency());
        }

        // For now, let's just return zero or implement a basic fixed/percent logic if available in actions
        // In a full implementation, we'd have an ActionEvaluator.
        return Money.zero(cart.getTotalAmount().getCurrency());
    }

    public String toJson() {
        // Placeholder for JSON serialization logic (e.g., using Jackson)
        return jsonConfig != null ? jsonConfig.toString() : "{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JsonPromotionRule that = (JsonPromotionRule) o;
        return java.util.Objects.equals(jsonConfig, that.jsonConfig);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), jsonConfig);
    }
}
