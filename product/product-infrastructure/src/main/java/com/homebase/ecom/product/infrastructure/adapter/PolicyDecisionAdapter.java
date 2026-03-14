package com.homebase.ecom.product.infrastructure.adapter;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import com.homebase.ecom.policy.client.PolicyClient;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.PimPolicyPort;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure adapter that connects the Product BC to the Policy BC.
 * Evaluates PIM policies using the PolicyClient (Feign).
 */
public class PolicyDecisionAdapter implements PimPolicyPort {

    private final PolicyClient policyClient;

    public PolicyDecisionAdapter(PolicyClient policyClient) {
        this.policyClient = policyClient;
    }

    @Override
    public boolean isAllowed(String policyId, Product product, Map<String, Object> additionalContext) {
        return evaluate(policyId, product, additionalContext).allowed();
    }

    @Override
    public PolicyDecision evaluate(String eventId, Product product, Map<String, Object> additionalContext) {
        EvaluateRequest request = new EvaluateRequest();
        
        // Subject is the Product
        request.setSubjectId(product.getId() != null ? product.getId().toString() : "new-product");
        request.setTargetModule("CATALOG");
        request.setAction(eventId);
        
        // Map product data into facts for the SpEL evaluation
        Map<String, Object> facts = new HashMap<>();
        if (additionalContext != null) {
            facts.putAll(additionalContext);
        }
        
        // Hydrate facts with product details
        facts.put("product.name", product.getName());
        facts.put("product.description", product.getDescription());
        facts.put("product.brand", product.getBrand());
        facts.put("product.categoryId", product.getCategoryId());
        facts.put("product.currentState", product.getCurrentState());
        
        // Add counts/lengths for common rules
        facts.put("product.nameLength", product.getName() != null ? product.getName().length() : 0);
        facts.put("product.mediaCount", product.getMedia() != null ? product.getMedia().size() : 0);
        facts.put("product.attributeCount", product.getAttributes() != null ? product.getAttributes().size() : 0);
        
        request.setFacts(facts);
        
        // Call the external policy microservice via Feign
        DecisionDto decisionDto = policyClient.evaluate(request);
        
        boolean allowed = decisionDto.getEffect() == com.homebase.ecom.policy.api.enums.Effect.ALLOW;
        return new PolicyDecision(allowed, decisionDto.getReasons());
    }
}
