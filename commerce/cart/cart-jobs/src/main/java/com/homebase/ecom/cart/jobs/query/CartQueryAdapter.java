package com.homebase.ecom.cart.jobs.query;

import org.chenile.query.model.ResponseRow;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.chenile.query.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter implementing CartQueryPort using Chenile SearchService client.
 *
 * Calls cart-query named queries via ProxyBuilder proxy.
 * Same-JVM: routed locally. Cross-service: routed via HTTP.
 * Returns cart IDs only — cart-jobs never loads full cart objects.
 */
public class CartQueryAdapter implements CartQueryPort {

    private static final Logger log = LoggerFactory.getLogger(CartQueryAdapter.class);

    private final SearchService cartSearchService;

    public CartQueryAdapter(SearchService cartSearchService) {
        this.cartSearchService = cartSearchService;
    }

    @Override
    public List<String> findActiveCartsWithVariant(String variantId) {
        log.debug("Querying active carts with variantId={}", variantId);
        Map<String, Object> filters = new HashMap<>();
        filters.put("variantId", variantId);
        return executeIdQuery("activeCartIdsWithVariant", filters);
    }

    @Override
    public List<String> findActiveCartsWithCoupon(String couponCode) {
        log.debug("Querying active carts with couponCode={}", couponCode);
        Map<String, Object> filters = new HashMap<>();
        filters.put("couponCode", couponCode);
        return executeIdQuery("activeCartIdsWithCoupon", filters);
    }

    @Override
    public List<String> findExpiredCarts() {
        log.debug("Querying expired carts");
        return executeIdQuery("expiredCartIds", new HashMap<>());
    }

    @Override
    public List<String> findIdleCarts(int thresholdHours) {
        log.debug("Querying idle carts with threshold={}h", thresholdHours);
        Map<String, Object> filters = new HashMap<>();
        filters.put("thresholdHours", thresholdHours);
        return executeIdQuery("idleCartIds", filters);
    }

    @SuppressWarnings("unchecked")
    private List<String> executeIdQuery(String queryName, Map<String, Object> filters) {
        try {
            SearchRequest<Map<String, Object>> request = new SearchRequest<>();
            request.setQueryName(queryName);
            request.setFilters(filters);

            SearchResponse response = cartSearchService.search(request);
            List<ResponseRow> rows = response.getList();
            if (rows == null || rows.isEmpty()) {
                return List.of();
            }

            List<String> ids = new ArrayList<>();
            for (ResponseRow responseRow : rows) {
                Object row = responseRow.getRow();
                if (row instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) row;
                    Object id = data.get("id");
                    if (id != null) {
                        ids.add(id.toString());
                    }
                }
            }
            log.debug("Query {} returned {} cart IDs", queryName, ids.size());
            return ids;
        } catch (Exception e) {
            log.error("Failed to execute query {}: {}", queryName, e.getMessage());
            return List.of();
        }
    }
}
