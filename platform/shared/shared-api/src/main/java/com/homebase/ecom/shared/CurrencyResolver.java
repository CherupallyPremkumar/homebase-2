package com.homebase.ecom.shared;

import java.util.Map;

/**
 * Resolves the currency for the current transaction context.
 * Follows priority: Request parameter > User preference > Tenant/Region default
 * > System default.
 */
public interface CurrencyResolver {

    /**
     * Resolves currency from the current execution context (ThreadLocal, etc).
     * 
     * @return Fully populated Currency object
     */
    Currency resolve();

    /**
     * Resolves currency based on a specific context map.
     */
    Currency resolve(Map<String, Object> context);

    /**
     * Seeds the ThreadLocal context with relevant details from a map.
     */
    void initContext(Map<String, Object> context);

    /**
     * Resolves the full Currency metadata based on a code.
     */
    Currency getMetadata(String code);
}
