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
 * OMS query controller -- cross-BC joined queries for Order Management System.
 * Joins orders, users, products, returns, tickets, settlements, fulfillment.
 */
@RestController
@ChenileController(value = "omsQuery", serviceName = "searchService")
public class OmsQueryController extends ControllerSupport {

    @SecurityConfig(authoritiesSupplier = "queryAuthorities")
    @PostMapping({ "/oms/{queryName}" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @PathVariable String queryName, @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        searchRequest.setQueryName(queryName);
        return this.process("search", request, new Object[] { queryName, searchRequest });
    }
}
