package com.homebase.ecom.shared.service.impl;

import com.homebase.ecom.shared.Currency;
import com.homebase.ecom.shared.CurrencyContext;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced implementation of CurrencyResolver with ThreadLocal context and caching.
 */
@Service
public class CurrencyResolverImpl implements CurrencyResolver {

    @Autowired
    private CconfigClient cconfigClient;

    // In-memory registry (could be moved to ISO417Registry class)
    private final Map<String, Currency> currencyRegistry = new HashMap<>();

    public CurrencyResolverImpl() {
        // Initial bootstrap - JPY has 0 precision, others have 2
        currencyRegistry.put("INR", new Currency("INR", "₹", 2));
        currencyRegistry.put("USD", new Currency("USD", "$", 2));
        currencyRegistry.put("JPY", new Currency("JPY", "¥", 0));
        currencyRegistry.put("EUR", new Currency("EUR", "€", 2));
        currencyRegistry.put("GBP", new Currency("GBP", "£", 2));
    }

    @Override
    public Currency resolve() {
        // 1. High Priority: ThreadLocal Context
        Currency contextCurrency = CurrencyContext.get();
        if (contextCurrency != null) {
            return contextCurrency;
        }

        // 2. Fallback: Base resolution via simulated empty context
        return resolve(new HashMap<>());
    }

    @Override
    public Currency resolve(Map<String, Object> context) {
        String code = resolveCode(context);
        return getMetadata(code);
    }

    private String resolveCode(Map<String, Object> context) {
        // 1. Check Request Context
        if (context.containsKey("requested_currency")) {
            return (String) context.get("requested_currency");
        }

        // 2. Check User Preferences (would normally call UserService)
        if (context.containsKey("userId")) {
            // Mock: User preference lookup
            // return userService.getPreferredCurrency(userId);
        }

        // 3. Check Tenant Configuration via Cconfig
        try {
            Map<String, Object> tenantConfig = cconfigClient.value("system", null);
            if (tenantConfig != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode node = mapper.valueToTree(tenantConfig);
                com.fasterxml.jackson.databind.JsonNode defaultNode = node.at("/settings/currency/default");
                if (!defaultNode.isMissingNode() && defaultNode.isTextual()) {
                    return defaultNode.asText();
                }
            }
        } catch (Exception e) {
            // Fall through
        }

        // 4. Global System Fallback — read from cconfig 'system' module (already covered above)
        //    No-op; second call to same key is a duplicate. Fallthrough to hardcoded default.
        // Absolute last resort — only if cconfig is unreachable
        return "INR";
    }

    @Override
    public void initContext(Map<String, Object> context) {
        Currency currency = resolve(context);
        CurrencyContext.set(currency);
    }

    @Override
    @Cacheable(value = "currencyMetadata", key = "#code")
    public Currency getMetadata(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Currency code must not be null or blank. Check CurrencyResolver chain.");
        }
        
        Currency currency = currencyRegistry.get(code.toUpperCase());
        if (currency == null) {
            // In a real system, we might look this up from a DB or external ISO service
            // For now, throw validation error to prevent downstream issues
            throw new IllegalArgumentException("Unsupported or invalid currency code: " + code);
        }
        return currency;
    }
}
