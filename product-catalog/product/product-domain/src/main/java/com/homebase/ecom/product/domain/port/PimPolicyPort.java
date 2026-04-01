package com.homebase.ecom.product.domain.port;

import com.homebase.ecom.product.domain.model.Product;
import java.util.Map;

/**
 * Outbound port for evaluating PIM-specific business rules and policies.
 * Abstracts the underlying policy engine (e.g., the 'policy' bounded context).
 */
public interface PimPolicyPort {

    /**
     * Evaluates a specific policy against a product context.
     * 
     * @param policyId The ID of the policy to evaluate (e.g., "pim-submit-review")
     * @param product The product being evaluated
     * @param additionalContext Any extra data needed for evaluation (e.g., userId, role)
     * @return true if the policy allows the action, false otherwise
     */
    boolean isAllowed(String policyId, Product product, Map<String, Object> additionalContext);

    /**
     * Evaluates a policy and returns the reason for the decision.
     */
    PolicyDecision evaluate(String policyId, Product product, Map<String, Object> additionalContext);

    /**
     * Represents the result of a policy evaluation.
     */
    record PolicyDecision(boolean allowed, String reason) {}
}
