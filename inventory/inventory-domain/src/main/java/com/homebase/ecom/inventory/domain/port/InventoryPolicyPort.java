package com.homebase.ecom.inventory.domain.port;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import java.util.Map;

/**
 * Outbound port for evaluating inventory-specific business rules and policies.
 * Abstracts the underlying policy engine (e.g., the 'policy' bounded context).
 */
public interface InventoryPolicyPort {

    /**
     * Evaluates a specific policy against an inventory context.
     *
     * @param policyId The ID of the policy to evaluate (e.g., "inventory-approve-stock")
     * @param inventoryItem The inventory item being evaluated
     * @param additionalContext Any extra data needed for evaluation (e.g., userId, role)
     * @return true if the policy allows the action, false otherwise
     */
    boolean isAllowed(String policyId, InventoryItem inventoryItem, Map<String, Object> additionalContext);

    /**
     * Evaluates a policy and returns the reason for the decision.
     */
    PolicyDecision evaluate(String policyId, InventoryItem inventoryItem, Map<String, Object> additionalContext);

    /**
     * Represents the result of a policy evaluation.
     */
    record PolicyDecision(boolean allowed, String reason) {}
}
