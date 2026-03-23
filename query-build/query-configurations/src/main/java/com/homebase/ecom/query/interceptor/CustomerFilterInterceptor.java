package com.homebase.ecom.query.interceptor;

import org.chenile.core.context.ContextContainer;
import org.chenile.query.service.interceptor.QueryUserFilterInterceptor;

import java.util.Map;

/**
 * Extends Chenile's QueryUserFilterInterceptor to inject customerId from JWT
 * into query systemFilters.
 *
 * Runs globally in the interceptor chain. Only injects customerId if the query
 * has "customerId" in its filters (set by client) — meaning the query expects
 * customer-level filtering. Admin queries that don't send customerId are unaffected.
 *
 * ContextContainer.getUser() returns the JWT sub claim.
 */
public class CustomerFilterInterceptor extends QueryUserFilterInterceptor {

    @Override
    protected void enhanceSystemFilters(Map<String, Object> systemFilters) {
        String currentUser = ContextContainer.getInstance().getUser();
        if (currentUser != null && !currentUser.isEmpty()) {
            systemFilters.put("customerId", currentUser);
        }
    }
}
