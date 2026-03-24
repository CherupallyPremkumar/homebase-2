package com.homebase.ecom.query.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.annotation.InterceptedBy;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Storefront query controller — cross-service joined queries for the storefront UI.
 * These queries join across product, inventory, pricing, offer tables
 * to return denormalized data that the storefront needs in a single call.
 */
@RestController
@ChenileController(value = "storefrontQuery", serviceName = "searchService")
public class StorefrontQueryController extends ControllerSupport {

    @SecurityConfig(authoritiesSupplier = "queryAuthorities")
    @PostMapping({ "/storefront/{queryName}" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @PathVariable String queryName, @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        return this.process("search", request, new Object[] { queryName, searchRequest });
    }

    @SecurityConfig(authoritiesSupplier = "queryAuthorities")
    @PostMapping({ "/storefront" })
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request,
            @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        return this.process("search", request, new Object[] { searchRequest.getQueryName(), searchRequest });
    }
}
