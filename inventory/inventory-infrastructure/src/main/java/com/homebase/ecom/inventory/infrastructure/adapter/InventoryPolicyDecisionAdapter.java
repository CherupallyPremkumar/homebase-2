package com.homebase.ecom.inventory.infrastructure.adapter;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import com.homebase.ecom.policy.api.service.DecisionService;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryPolicyPort;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure adapter that connects the Inventory BC to the Policy BC.
 * Evaluates inventory policies using the DecisionService (Chenile proxy).
 */
public class InventoryPolicyDecisionAdapter implements InventoryPolicyPort {

    private final DecisionService decisionService;

    public InventoryPolicyDecisionAdapter(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @Override
    public boolean isAllowed(String policyId, InventoryItem inventoryItem, Map<String, Object> additionalContext) {
        return evaluate(policyId, inventoryItem, additionalContext).allowed();
    }

    @Override
    public PolicyDecision evaluate(String eventId, InventoryItem inventoryItem, Map<String, Object> additionalContext) {
        EvaluateRequest request = new EvaluateRequest();

        request.setSubjectId(inventoryItem.getId() != null ? inventoryItem.getId().toString() : "new-inventory-item");
        request.setAction(eventId);

        if (additionalContext != null && additionalContext.containsKey("policyId")) {
            request.setPolicyId((String) additionalContext.get("policyId"));
        } else {
            request.setTargetModule("INVENTORY");
        }

        Map<String, Object> facts = new HashMap<>();
        if (additionalContext != null) {
            additionalContext.entrySet().stream()
                    .filter(e -> !"policyId".equals(e.getKey()))
                    .forEach(e -> facts.put(e.getKey(), e.getValue()));
        }

        facts.put("sku", inventoryItem.getSku());
        facts.put("productId", inventoryItem.getProductId());
        facts.put("variantId", inventoryItem.getVariantId());
        facts.put("quantity", inventoryItem.getQuantity());
        facts.put("availableQuantity", inventoryItem.getAvailableQuantity());
        facts.put("reservedQuantity", inventoryItem.getReservedQuantity());
        facts.put("damagedQuantity", inventoryItem.getDamagedQuantity());
        facts.put("inboundQuantity", inventoryItem.getInboundQuantity());
        facts.put("damagePercentage", inventoryItem.getDamagePercentage());
        facts.put("isLowStock", inventoryItem.isLowStock());
        facts.put("isOutOfStock", inventoryItem.isOutOfStock());
        facts.put("isFbaEnabled", inventoryItem.getIsFbaEnabled());
        facts.put("currentState", inventoryItem.getCurrentState() != null ? inventoryItem.getCurrentState().toString() : null);

        request.setFacts(facts);

        DecisionDto decisionDto = decisionService.evaluate(request);

        boolean allowed = decisionDto.getEffect() == com.homebase.ecom.policy.api.enums.Effect.ALLOW;
        return new PolicyDecision(allowed, decisionDto.getReasons());
    }
}
