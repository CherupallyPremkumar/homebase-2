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
 * Seller dashboard query controller -- seller-specific queries.
 * Reads from hm_query.* OLAP tables:
 * seller_dashboard_flat, order_details_flat, order_items_flat,
 * settlement_flat, product_catalog_flat, return_detail_flat.
 */
@RestController
@ChenileController(value = "sellerDashboardQuery", serviceName = "searchService")
public class SellerDashboardQueryController extends ControllerSupport {

    @SecurityConfig(authoritiesSupplier = "queryAuthorities")
    @PostMapping({ "/seller-dashboard/{queryName}" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @PathVariable String queryName, @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        searchRequest.setQueryName(queryName);
        return this.process("search", request, new Object[] { queryName, searchRequest });
    }
}
