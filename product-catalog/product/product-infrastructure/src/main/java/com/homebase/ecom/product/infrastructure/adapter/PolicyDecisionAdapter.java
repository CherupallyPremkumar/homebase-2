package com.homebase.ecom.product.infrastructure.adapter;

import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import com.homebase.ecom.rulesengine.api.service.DecisionService;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.PimPolicyPort;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure adapter that connects the Product BC to the Policy BC.
 * Evaluates PIM policies using the DecisionService (Chenile proxy).
 */
public class PolicyDecisionAdapter implements PimPolicyPort {

    private final DecisionService decisionService;

    public PolicyDecisionAdapter(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @Override
    public boolean isAllowed(String policyId, Product product, Map<String, Object> additionalContext) {
        return evaluate(policyId, product, additionalContext).allowed();
    }

    @Override
    public PolicyDecision evaluate(String eventId, Product product, Map<String, Object> additionalContext) {
        EvaluateRequest request = new EvaluateRequest();

        request.setSubjectId(product.getId() != null ? product.getId().toString() : "new-product");
        request.setAction(eventId);

        // Use policyId from context if provided (resolved by cconfig), otherwise fall back to targetModule
        if (additionalContext != null && additionalContext.containsKey("policyId")) {
            request.setRuleSetId((String) additionalContext.get("policyId"));
        } else {
            request.setTargetModule("PRODUCT");
        }

        Map<String, Object> facts = new HashMap<>();
        if (additionalContext != null) {
            additionalContext.entrySet().stream()
                    .filter(e -> !"policyId".equals(e.getKey()))
                    .forEach(e -> facts.put(e.getKey(), e.getValue()));
        }

        facts.put("productName", product.getName());
        facts.put("productDescription", product.getDescription());
        facts.put("productBrand", product.getBrand());
        facts.put("productCategoryId", product.getCategoryId());
        facts.put("productCurrentState", product.getCurrentState() != null ? product.getCurrentState().toString() : null);
        facts.put("productNameLength", product.getName() != null ? product.getName().length() : 0);
        facts.put("productDescriptionLength", product.getDescription() != null ? product.getDescription().length() : 0);
        facts.put("productMediaCount", product.getMedia() != null ? product.getMedia().size() : 0);
        facts.put("productAttributeCount", product.getAttributes() != null ? product.getAttributes().size() : 0);
        facts.put("productVariantCount", product.getVariants() != null ? product.getVariants().size() : 0);

        request.setFacts(facts);

        DecisionDto decisionDto = decisionService.evaluate(request);

        boolean allowed = decisionDto.getEffect() == com.homebase.ecom.rulesengine.api.enums.Effect.ALLOW;
        return new PolicyDecision(allowed, decisionDto.getReasons());
    }
}
