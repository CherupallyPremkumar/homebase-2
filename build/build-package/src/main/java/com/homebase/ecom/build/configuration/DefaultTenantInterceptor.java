package com.homebase.ecom.build.configuration;

import org.chenile.core.context.ChenileExchange;
import org.chenile.core.context.ContextContainer;
import org.chenile.core.context.HeaderUtils;
import org.chenile.core.interceptors.BaseChenileInterceptor;

/**
 * Sets the default tenant to "homebase" when no tenant header is provided.
 * Runs after populateContextContainer in the Chenile pre-processor chain.
 */
public class DefaultTenantInterceptor extends BaseChenileInterceptor {

    private static final String DEFAULT_TENANT = "homebase";

    @Override
    protected void doPreProcessing(ChenileExchange exchange) {
        ContextContainer ctx = ContextContainer.getInstance();
        String tenant = ctx.getTenant();
        if (tenant == null || tenant.trim().isEmpty()) {
            ctx.setTenant(DEFAULT_TENANT);
            exchange.setHeader(HeaderUtils.TENANT_ID_KEY, DEFAULT_TENANT);
        }
        super.doPreProcessing(exchange);
    }
}
