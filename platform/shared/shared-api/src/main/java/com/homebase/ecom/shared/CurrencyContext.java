package com.homebase.ecom.shared;

import org.chenile.core.context.ContextContainer;

/**
 * Manages the currency context for the current thread of execution.
 * Leverages Chenile ContextContainer for thread-local storage and automatic
 * cleanup.
 */
public class CurrencyContext {

    private static final String CURRENCY_KEY = "com.homebase.currency";

    /**
     * Sets the currency for the current thread.
     */
    public static void set(Currency currency) {
        ContextContainer.putExtension(CURRENCY_KEY, currency);
    }

    /**
     * Returns the currency for the current thread, or null if not set.
     */
    public static Currency get() {
        return (Currency) ContextContainer.getExtension(CURRENCY_KEY);
    }

    /**
     * Clears the currency for the current thread.
     * Note: Chenile ContextContainer usually clears extensions at the end of the
     * request.
     */
    public static void clear() {
        ContextContainer.getContextExtensions().remove(CURRENCY_KEY);
    }
}
