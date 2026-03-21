package com.homebase.ecom.cart.infrastructure.persistence.adapter;

import com.homebase.ecom.cart.port.InventoryCheckPort;
import org.chenile.query.model.ResponseRow;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.chenile.query.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Infrastructure adapter for inventory availability checks.
 * Uses Chenile SearchService (CQRS query) via ProxyBuilder proxy.
 * Calls the "check-availability" named query defined in inventory.json.
 */
public class InventoryCheckAdapter implements InventoryCheckPort {

    private static final Logger log = LoggerFactory.getLogger(InventoryCheckAdapter.class);

    private final SearchService inventorySearchService;

    public InventoryCheckAdapter(SearchService inventorySearchService) {
        this.inventorySearchService = inventorySearchService;
    }

    @Override
    public boolean isAvailable(String variantId, int quantity) {
        return getAvailableQuantity(variantId) >= quantity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getAvailableQuantity(String variantId) {
        log.debug("Querying inventory availability for variantId={}", variantId);
        try {
            SearchRequest<Map<String, Object>> request = new SearchRequest<>();
            request.setQueryName("check-availability");
            Map<String, Object> filters = new HashMap<>();
            filters.put("variantId", variantId);
            request.setFilters(filters);
            request.setNumRowsInPage(1);

            SearchResponse response = inventorySearchService.search(request);
            List<ResponseRow> rows = response.getList();
            if (rows == null || rows.isEmpty()) {
                log.debug("No inventory found for variantId={}", variantId);
                return 0;
            }

            Object row = rows.get(0).getRow();
            if (row instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) row;
                int available = ((Number) data.getOrDefault("availableQuantity", 0)).intValue();
                int reserved = ((Number) data.getOrDefault("reserved", 0)).intValue();
                return available - reserved;
            }
            return 0;
        } catch (Exception e) {
            log.error("Failed to query inventory for variantId={}: {}", variantId, e.getMessage());
            return 0;
        }
    }
}
