package com.homebase.ecom.shared.service.impl;

import com.homebase.ecom.shared.ExchangeRateService;
import com.homebase.ecom.shared.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
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

        // TODO: Rounding and scale based on target currency metadata
        // Rates are defined in major units (e.g., 1 USD = 83.21 INR).
        // Money stores amounts in smallest currency units (paise, cents, yen).
        // To convert correctly across currencies with different fraction digits:
        //   result_smallest = (source_smallest / source_divisor) * rate * target_divisor
        // This ensures JPY (0 decimals), BHD (3 decimals), INR/USD (2 decimals) all convert correctly.
        int sourceFractionDigits = getDefaultFractionDigits(amount.getCurrency());
        int targetFractionDigits = getDefaultFractionDigits(targetCurrency);

        BigDecimal sourceDivisor = BigDecimal.TEN.pow(sourceFractionDigits);
        BigDecimal targetDivisor = BigDecimal.TEN.pow(targetFractionDigits);

        // Convert: source smallest units → major units → target major units → target smallest units
        long convertedAmount = BigDecimal.valueOf(amount.getAmount())
                .multiply(rate)
                .multiply(targetDivisor)
                .divide(sourceDivisor, 0, RoundingMode.HALF_UP)
                .longValueExact();

        return Money.of(convertedAmount, targetCurrency);
    }

    /**
     * Returns the ISO 4217 default fraction digits for the given currency code.
     * Falls back to 2 for unknown currencies.
     */
    private int getDefaultFractionDigits(String currencyCode) {
        try {
            return Currency.getInstance(currencyCode).getDefaultFractionDigits();
        } catch (IllegalArgumentException e) {
            return 2; // safe default
        }
    }
}
