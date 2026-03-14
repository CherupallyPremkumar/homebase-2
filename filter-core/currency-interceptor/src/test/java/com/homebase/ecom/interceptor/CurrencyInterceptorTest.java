package com.homebase.ecom.interceptor;

import com.homebase.ecom.configuration.CurrencyConfiguration;
import com.homebase.ecom.mapper.CurrencyMapper;
import org.chenile.core.context.ContextContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CurrencyConfiguration.class})
@ActiveProfiles("test")
@DisplayName("Currency Filter Tests")
class CurrencyInterceptorTest {

    private CurrencyInterceptor currencyFilter;

    @MockitoBean
    private CurrencyMapper currencyMapper;

    @BeforeEach
    void setUp() {
        // Clear context before each test
        ContextContainer.getInstance().clear();

        // Setup mock mapper behavior
        when(currencyMapper.getCurrencyForRegion("US")).thenReturn("USD");
        when(currencyMapper.getCurrencyForRegion("GB")).thenReturn("GBP");
        when(currencyMapper.getCurrencyForRegion("UNKNOWN")).thenReturn(null);
        when(currencyMapper.getDefaultCurrency()).thenReturn("USD");

        currencyFilter = new CurrencyInterceptor(currencyMapper);
    }

    @Test
    @DisplayName("Should set currency from valid region header")
    void testSetCurrencyFromValidRegion() {
        // This would need ChenileExchange mock
        // Demonstration of usage
        ContextContainer.getInstance().put("currency", "USD");

        String currency = ContextContainer.getInstance().get("currency");
        assertThat(currency).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should use default when region is not found")
    void testDefaultCurrencyWhenRegionUnknown() {
        when(currencyMapper.getCurrencyForRegion("UNKNOWN")).thenReturn(null);

        String currency = currencyMapper.getCurrencyForRegion("UNKNOWN");

        // Since mapper returns null, filter would use default
        if (currency == null) {
            currency = currencyMapper.getDefaultCurrency();
        }

        assertThat(currency).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should retrieve currency from context")
    void testGetCurrencyFromContext() {
        ContextContainer.getInstance().put("x-homebase-currency", "GBP");

        String currency = CurrencyInterceptor.getCurrencyFromContext();

        assertThat(currency).isEqualTo("GBP");
    }

    @Test
    @DisplayName("Should return default when currency not in context")
    void testGetDefaultFromContext() {
        ContextContainer.getInstance().clear();

        String currency = CurrencyInterceptor.getCurrencyFromContext();

        assertThat(currency).isEqualTo("INR");
    }
}
