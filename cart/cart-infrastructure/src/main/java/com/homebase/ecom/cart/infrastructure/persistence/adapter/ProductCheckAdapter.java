package com.homebase.ecom.cart.infrastructure.persistence.adapter;

import com.homebase.ecom.cart.port.ProductCheckPort;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.chenile.query.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure adapter for variant/product existence checks.
 * Uses Chenile SearchService (CQRS query) via ProxyBuilder proxy.
 * Calls the "variant-exists" named query — checks variant exists
 * under the given product AND product is in sellable state.
 */
public class ProductCheckAdapter implements ProductCheckPort {

    private static final Logger log = LoggerFactory.getLogger(ProductCheckAdapter.class);

    private final SearchService productSearchService;

    public ProductCheckAdapter(SearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @Override
    public boolean variantExists(String productId, String variantId) {
        log.debug("Checking variant existence: productId={}, variantId={}", productId, variantId);
        try {
            SearchRequest<Map<String, Object>> request = new SearchRequest<>();
            request.setQueryName("variant-exists");
            Map<String, Object> filters = new HashMap<>();
            filters.put("productId", productId);
            filters.put("variantId", variantId);
            request.setFilters(filters);
            request.setNumRowsInPage(1);

            SearchResponse response = productSearchService.search(request);
            boolean found = response.getList() != null && !response.getList().isEmpty();
            log.debug("Variant {} under product {} — {}", variantId, productId,
                    found ? "exists (product active)" : "not found or product inactive");
            return found;
        } catch (Exception e) {
            log.error("Failed to check variant existence: productId={}, variantId={}: {}",
                    productId, variantId, e.getMessage());
            return false;
        }
    }
}
