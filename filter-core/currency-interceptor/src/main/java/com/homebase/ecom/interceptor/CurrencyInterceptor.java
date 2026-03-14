package com.homebase.ecom.interceptor;

import com.homebase.ecom.mapper.CurrencyMapper;
import org.chenile.core.context.ChenileExchange;
import org.chenile.core.context.ContextContainer;
import org.chenile.core.interceptors.BaseChenileInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Enhanced Currency Interceptor that uses BOTH Locale and Region Header
 *
 * Priority order for determining region:
 * 1. Explicit x-chenile-region-id header (highest priority)
 * 2. Country code from Locale (from Accept-Language)
 * 3. Default to "US"
 *
 * This allows:
 * - Explicit region override via header
 * - Automatic locale detection from Accept-Language
 * - Safe fallback to US
 */

public class CurrencyInterceptor extends BaseChenileInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyInterceptor.class);

    private static final String REGION_HEADER = "x-chenile-region-id";
    private static final String CURRENCY_KEY = "x-homebase-currency";
    private static final String DEAFULT="INR";

    private final CurrencyMapper currencyMapper;


    @Autowired
    public CurrencyInterceptor(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Override
    protected void doPreProcessing(ChenileExchange exchange) {
        try {
            // Determine region using priority order
            String region = resolveRegion(exchange);
            String currency = mapRegionToCurrency(region);
            if (currency != null) {
                ContextContainer.getInstance().put(CURRENCY_KEY, currency);
            } else {
                String defaultCurrency = currencyMapper.getDefaultCurrency();
                ContextContainer.getInstance().put(CURRENCY_KEY, defaultCurrency);
            }
        } catch (Exception e) {
            ContextContainer.getInstance().put(CURRENCY_KEY, currencyMapper.getDefaultCurrency());
        }
        super.doPreProcessing(exchange);
    }

    private String resolveRegion(ChenileExchange exchange) {
        String regionHeader = exchange.getHeader(REGION_HEADER, String.class);
        if (regionHeader != null && !regionHeader.trim().isEmpty()) {
            return regionHeader.trim();
        }
        Locale locale = exchange.getLocale();
        if (locale != null && !locale.getCountry().isEmpty()) {
            String countryCode = locale.getCountry();
            return countryCode;
        }
        return currencyMapper.getDefaultCurrency();
    }


    private String mapRegionToCurrency(String region) {
        if (region == null || region.trim().isEmpty()) {
            return null;
        }
        return currencyMapper.getCurrencyForRegion(region);
    }

    @Override
    protected void doPostProcessing(ChenileExchange exchange) {
        String currency = ContextContainer.getInstance().get(CURRENCY_KEY);
        if (currency != null) {
            exchange.setHeader("X-Currency", currency);
        }
        super.doPostProcessing(exchange);
    }


    public static String getCurrencyFromContext() {
        String currency = ContextContainer.getInstance().get(CURRENCY_KEY);
        return (currency != null && !currency.isEmpty()) ? currency : DEAFULT;
    }
}