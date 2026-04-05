package com.homebase.ecom.query.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Platform dashboard query controller -- admin-only aggregation queries.
 * Reads from hm_query.* computed aggregation tables:
 * platform_kpi_snapshots, live_order_pipeline, top_products_today,
 * top_sellers_today, category_performance_daily, seller_health_summary_daily,
 * conversion_funnel_daily, revenue_by_region_daily.
 */
@RestController
@ChenileController(value = "platformDashboardQuery", serviceName = "searchService")
public class PlatformDashboardQueryController extends ControllerSupport {

    @SecurityConfig(authoritiesSupplier = "queryAuthorities")
    @PostMapping({ "/platform-dashboard/{queryName}" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @PathVariable String queryName, @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        searchRequest.setQueryName(queryName);
        return this.process("search", request, new Object[] { queryName, searchRequest });
    }
}
