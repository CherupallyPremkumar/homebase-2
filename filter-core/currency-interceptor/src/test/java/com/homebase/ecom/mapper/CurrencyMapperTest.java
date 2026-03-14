package com.homebase.ecom.mapper;

import com.homebase.ecom.configuration.CurrencyConfiguration;
import com.homebase.ecom.configuration.CurrencyProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CurrencyConfiguration.class})
@ActiveProfiles("test")
@DisplayName("Currency Mapper Tests")
class CurrencyMapperTest {

    private CurrencyMapper currencyMapper;

    @MockitoBean
    private CurrencyProperties currencyProperties;

    @BeforeEach
    void setUp() {
        // Initialize with mock properties
        Map<String, String> configMappings = new HashMap<>();
        configMappings.put("US", "USD");
        configMappings.put("GB", "GBP");
        configMappings.put("EU", "EUR");
        configMappings.put("default", "USD");

        when(currencyProperties.getMapping()).thenReturn(configMappings);
        when(currencyProperties.getDefaultCurrency()).thenReturn("USD");

        currencyMapper = new CurrencyMapper(currencyProperties);
    }

    @Test
    @DisplayName("Should map region to currency from config")
    void testGetCurrencyFromConfig() {
        String currency = currencyMapper.getCurrencyForRegion("US");
        assertThat(currency).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should be case-insensitive")
    void testCaseInsensitiveRegion() {
        String currency = currencyMapper.getCurrencyForRegion("gb");
        assertThat(currency).isEqualTo("GBP");

        String currency2 = currencyMapper.getCurrencyForRegion("Gb");
        assertThat(currency2).isEqualTo("GBP");
    }

    @Test
    @DisplayName("Should use hardcoded fallback when config is missing")
    void testHardcodedFallback() {
        // Remove "JP" from config (it's in hardcoded)
        Map<String, String> configMappings = new HashMap<>();
        when(currencyProperties.getMapping()).thenReturn(configMappings);

        currencyMapper = new CurrencyMapper(currencyProperties);

        String currency = currencyMapper.getCurrencyForRegion("JP");
        assertThat(currency).isEqualTo("JPY");
    }

    @Test
    @DisplayName("Should return null for unknown region")
    void testUnknownRegion() {
        String currency = currencyMapper.getCurrencyForRegion("UNKNOWN_XYZ");
        assertThat(currency).isNull();
    }

    @Test
    @DisplayName("Should handle null region")
    void testNullRegion() {
        String currency = currencyMapper.getCurrencyForRegion(null);
        assertThat(currency).isNull();
    }

    @Test
    @DisplayName("Should handle empty region")
    void testEmptyRegion() {
        String currency = currencyMapper.getCurrencyForRegion("");
        assertThat(currency).isNull();
    }

    @Test
    @DisplayName("Should handle whitespace-only region")
    void testWhitespaceRegion() {
        String currency = currencyMapper.getCurrencyForRegion("   ");
        assertThat(currency).isNull();
    }

    @Test
    @DisplayName("Should check if mapping exists")
    void testHasMapping() {
        assertThat(currencyMapper.hasMapping("US")).isTrue();
        assertThat(currencyMapper.hasMapping("GB")).isTrue();
        assertThat(currencyMapper.hasMapping("JP")).isTrue(); // hardcoded
        assertThat(currencyMapper.hasMapping("UNKNOWN")).isFalse();
    }

    @Test
    @DisplayName("Should get all mappings (config + hardcoded)")
    void testGetAllMappings() {
        Map<String, String> allMappings = currencyMapper.getAllMappings();

        // Should include config mappings
        assertThat(allMappings).containsEntry("US", "USD");
        assertThat(allMappings).containsEntry("GB", "GBP");

        // Should include hardcoded fallbacks
        assertThat(allMappings).containsEntry("JP", "JPY");
        assertThat(allMappings).containsEntry("AU", "AUD");
    }

    @Test
    @DisplayName("Should return default currency")
    void testGetDefaultCurrency() {
        String defaultCurrency = currencyMapper.getDefaultCurrency();
        assertThat(defaultCurrency).isEqualTo("USD");
    }

    @Test
    @DisplayName("Config should override hardcoded mappings")
    void testConfigOverridesHardcoded() {
        // Set config to override hardcoded JP mapping
        Map<String, String> configMappings = new HashMap<>();
        configMappings.put("JP", "JPY_CUSTOM");

        when(currencyProperties.getMapping()).thenReturn(configMappings);
        currencyMapper = new CurrencyMapper(currencyProperties);

        String currency = currencyMapper.getCurrencyForRegion("JP");
        assertThat(currency).isEqualTo("JPY_CUSTOM"); // From config, not hardcoded
    }
}

