package com.homebase.ecom.shared.service.impl;

import com.homebase.ecom.shared.ExchangeRateService;
import com.homebase.ecom.shared.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategy-based ExchangeRateService. 
 * Production approach would involve caching and external API lookups.
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Map<String, BigDecimal> rates = new HashMap<>();

    public ExchangeRateServiceImpl() {
        // Mocked rates for now
        rates.put("USD_INR", new BigDecimal("83.21"));
        rates.put("INR_USD", new BigDecimal("0.012"));
        rates.put("USD_JPY", new BigDecimal("150.12"));
    }

    @Override
    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) return BigDecimal.ONE;
        
        String key = fromCurrency + "_" + toCurrency;
        if (rates.containsKey(key)) return rates.get(key);
        
        // Inverse rate if available
        String inverseKey = toCurrency + "_" + fromCurrency;
        if (rates.containsKey(inverseKey)) {
            return BigDecimal.ONE.divide(rates.get(inverseKey), 6, RoundingMode.HALF_UP);
        }

        throw new RuntimeException("Exchange rate not found for " + key);
    }

    @Override
    public Money convert(Money amount, String targetCurrency) {
        if (amount.getCurrency().equals(targetCurrency)) return amount;

        BigDecimal rate = getRate(amount.getCurrency(), targetCurrency);
        BigDecimal convertedAmount = amount.getAmount().multiply(rate);
        
        // TODO: Rounding and scale based on target currency metadata
        return new Money(convertedAmount, targetCurrency);
    }
}
