package com.homebase.ecom.shared;

import java.math.BigDecimal;

/**
 * Service for currency conversion and exchange rate management.
 */
public interface ExchangeRateService {

    /**
     * Gets the exchange rate from base to target currency.
     */
    BigDecimal getRate(String fromCurrency, String toCurrency);

    /**
     * Converts an amount from one currency to another.
     */
    Money convert(Money amount, String targetCurrency);
}
