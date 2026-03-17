package com.homebase.ecom.pricing.domain.port;

import java.util.List;
import java.util.Map;

/**
 * Port for evaluating pricing rules via the centralized Policy service.
 * Pricing sends facts (subtotal, quantity, tier, etc.) and gets back
 * all matching discount decisions with metadata (action, value, scope).
 *
 * Infrastructure adapter calls Policy's DecisionService.evaluateAll()
 * via ProxyBuilder client.
 */
public interface PolicyEvaluationPort {

    /**
     * Evaluate all active pricing policies against the given facts.
     *
     * @param facts pricing context as key-value pairs
     * @return list of matching decisions, each with metadata containing
     *         discountAction (PERCENTAGE/FLAT), discountValue, discountScope, etc.
     */
    List<PolicyDecision> evaluateDiscountRules(Map<String, Object> facts);

    /**
     * A single discount decision from the Policy engine.
     */
    class PolicyDecision {
        private String ruleId;
        private String ruleName;
        private String effect;     // ALLOW or DENY
        private String reasons;
        private Map<String, String> metadata; // discountAction, discountValue, discountScope, etc.

        public PolicyDecision() {}

        public String getRuleId() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId = ruleId; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public String getEffect() { return effect; }
        public void setEffect(String effect) { this.effect = effect; }
        public String getReasons() { return reasons; }
        public void setReasons(String reasons) { this.reasons = reasons; }
        public Map<String, String> getMetadata() { return metadata; }
        public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

        public String getMetaValue(String key) {
            return metadata != null ? metadata.get(key) : null;
        }

        public String getMetaValue(String key, String defaultValue) {
            if (metadata == null) return defaultValue;
            return metadata.getOrDefault(key, defaultValue);
        }
    }
}
