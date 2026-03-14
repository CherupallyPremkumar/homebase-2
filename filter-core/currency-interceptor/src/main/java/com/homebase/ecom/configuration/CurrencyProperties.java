package com.homebase.ecom.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "currency")
public class CurrencyProperties {

    private Map<String, String> mapping = new HashMap<>();

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public String getDefaultCurrency() {
        return mapping.getOrDefault("default", "INR");
    }
}