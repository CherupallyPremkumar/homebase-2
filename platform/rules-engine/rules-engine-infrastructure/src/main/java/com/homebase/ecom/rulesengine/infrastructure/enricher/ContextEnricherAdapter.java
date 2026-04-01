package com.homebase.ecom.rulesengine.infrastructure.enricher;

import com.homebase.ecom.rulesengine.api.dto.ContextDto;
import com.homebase.ecom.rulesengine.domain.service.ContextEnricher;

import java.util.HashMap;
import java.util.Map;

public class ContextEnricherAdapter implements ContextEnricher {
    @Override
    public Map<String, Object> enrich(ContextDto contextDto) {
        Map<String, Object> enriched = new HashMap<>();
        if (contextDto.getData() != null) {
            enriched.putAll(contextDto.getData());
        }
        enriched.put("tenantId", contextDto.getTenantId());
        enriched.put("userId", contextDto.getUserId());
        // Hydrate from DB or internal services if only IDs are provided
        enrichUser(enriched, contextDto.getUserId());
        enrichProduct(enriched);

        return enriched;
    }

    private void enrichUser(Map<String, Object> enriched, String userId) {
        if (!enriched.containsKey("user") && userId != null) {
            // Simulate fetching user from DB (UserProvider)
            Map<String, Object> user = new HashMap<>();
            user.put("id", userId);
            user.put("trustScore", 95);
            user.put("country", "India");
            user.put("isPremium", true);
            enriched.put("user", user);
        }
    }

    private void enrichProduct(Map<String, Object> enriched) {
        if (!enriched.containsKey("product") && enriched.containsKey("productId")) {
            // Simulate fetching product from DB (ProductProvider)
            Map<String, Object> product = new HashMap<>();
            product.put("id", enriched.get("productId"));
            product.put("price", 5000);
            product.put("category", "Silk");
            product.put("originState", "TN");
            product.put("stockCount", 50);
            enriched.put("product", product);
        }
    }
}
