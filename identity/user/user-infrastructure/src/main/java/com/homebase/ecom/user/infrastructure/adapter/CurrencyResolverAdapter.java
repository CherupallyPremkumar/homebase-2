package com.homebase.ecom.user.infrastructure.adapter;

import com.homebase.ecom.interceptor.CurrencyInterceptor;
import com.homebase.ecom.user.domain.port.CurrencyResolver;

/**
 * Inbound Adapter (Hexagonal Architecture) — implements the CurrencyResolver port.
 *
 * Bridges the pure domain port to the Chenile infrastructure:
 *   domain.CurrencyResolver ← user-infrastructure ← filter-core.CurrencyInterceptor ← ContextContainer
 *
 * Priority for currency resolution:
 *   1. x-chenile-region-id request header (explicit override)
 *   2. Country code from Accept-Language locale
 *   3. Default: INR (from CurrencyProperties)
 */
public class CurrencyResolverAdapter implements CurrencyResolver {

    @Override
    public String resolveCurrentCurrency() {
        return CurrencyInterceptor.getCurrencyFromContext();
    }
}
