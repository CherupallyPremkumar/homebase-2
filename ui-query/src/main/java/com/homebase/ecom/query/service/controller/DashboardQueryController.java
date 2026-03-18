package com.homebase.ecom.query.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard query controller — cross-service joined queries for the admin dashboard.
 * Revenue stats, order summaries, inventory health, supplier performance etc.
 */
@RestController
@ChenileController(value = "dashboardQuery", serviceName = "searchService")
public class DashboardQueryController extends ControllerSupport {

    @PostMapping({ "/dashboard/{queryName}" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @PathVariable String queryName, @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        return this.process("search", request, new Object[] { queryName, searchRequest });
    }

    @PostMapping({ "/dashboard" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        return this.process("search", request, new Object[] { searchRequest.getQueryName(), searchRequest });
    }
}
