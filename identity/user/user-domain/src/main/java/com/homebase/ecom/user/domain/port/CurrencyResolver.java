package com.homebase.ecom.user.domain.port;

/**
 * Outbound Port (Hexagonal): Currency resolution.
 *
 * The domain depends on this interface — NOT on CurrencyInterceptor directly.
 * The infrastructure adapter (CurrencyResolverAdapter) implements this
 * by reading from Chenile's ContextContainer via CurrencyInterceptor.
 *
 * This preserves the pure-domain / hexagonal boundary.
 */
public interface CurrencyResolver {
    /**
     * Returns the currency code for the current request context.
     * Falls back to "INR" if no context is available.
     */
    String resolveCurrentCurrency();
}
