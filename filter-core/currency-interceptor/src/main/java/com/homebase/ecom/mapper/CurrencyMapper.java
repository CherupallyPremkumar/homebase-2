package com.homebase.ecom.mapper;

import com.homebase.ecom.configuration.CurrencyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Component
public class CurrencyMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyMapper.class);

    private static final Map<String, String> HARDCODED_MAPPINGS = new HashMap<>();

    static {
        HARDCODED_MAPPINGS.put("US", "USD");
        HARDCODED_MAPPINGS.put("CA", "CAD");
        HARDCODED_MAPPINGS.put("MX", "MXN");
        HARDCODED_MAPPINGS.put("GB", "GBP");
        HARDCODED_MAPPINGS.put("EU", "EUR");
        HARDCODED_MAPPINGS.put("DE", "EUR");
        HARDCODED_MAPPINGS.put("FR", "EUR");
        HARDCODED_MAPPINGS.put("ES", "EUR");
        HARDCODED_MAPPINGS.put("IT", "EUR");
        HARDCODED_MAPPINGS.put("JP", "JPY");
        HARDCODED_MAPPINGS.put("AU", "AUD");
        HARDCODED_MAPPINGS.put("NZ", "NZD");
        HARDCODED_MAPPINGS.put("IN", "INR");
        HARDCODED_MAPPINGS.put("SG", "SGD");
        HARDCODED_MAPPINGS.put("BR", "BRL");
    }

    private final CurrencyProperties currencyProperties;

    @Autowired
    public CurrencyMapper(CurrencyProperties currencyProperties) {
        this.currencyProperties = currencyProperties;
    }

    public String getCurrencyForRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            return null;
        }
        String normalizedRegion = region.trim().toUpperCase();
        String configCurrency = currencyProperties.getMapping().get(normalizedRegion);
        if (configCurrency != null && !configCurrency.isEmpty()) {
            return configCurrency;
        }
        return HARDCODED_MAPPINGS.get(normalizedRegion);
    }


    public String getDefaultCurrency() {
        return currencyProperties.getDefaultCurrency();
    }


    public boolean hasMapping(String region) {
        if (region == null || region.trim().isEmpty()) {
            return false;
        }
        String normalizedRegion = region.trim().toUpperCase();
        return currencyProperties.getMapping().containsKey(normalizedRegion) ||
                HARDCODED_MAPPINGS.containsKey(normalizedRegion);
    }


    public Map<String, String> getAllMappings() {
        Map<String, String> combined = new HashMap<>(HARDCODED_MAPPINGS);
        combined.putAll(currencyProperties.getMapping());
        return combined;
    }


    public void logMappings() {
        Map<String, String> allMappings = getAllMappings();
        allMappings.forEach((region, currency) ->
                LOGGER.info("  {} -> {}", region, currency)
        );
    }
}
