package com.homebase.ecom.pricing.infrastructure.integration;

import com.homebase.ecom.pricing.domain.port.TaxCalculationPort;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.ToIntFunction;

/**
 * Infrastructure adapter: calculates tax on a taxable amount.
 * Accepts a category-to-rate resolver function so the service layer
 * can inject cconfig-driven GST rates without a circular dependency.
 * Will be replaced with a real adapter calling tax-client when the Tax module
 * provides full HSN-based GST computation.
 */
public class TaxCalculationAdapter implements TaxCalculationPort {

    private static final Logger log = LoggerFactory.getLogger(TaxCalculationAdapter.class);

    private final ToIntFunction<String> categoryGstRateResolver;

    /**
     * @param categoryGstRateResolver resolves GST rate (as integer percentage) for a product category.
     *                                 Null category should return the default rate.
     */
    public TaxCalculationAdapter(ToIntFunction<String> categoryGstRateResolver) {
        this.categoryGstRateResolver = categoryGstRateResolver;
    }

    @Override
    public Money calculateTax(Money taxableAmount, String region, String productCategory) {
        int gstRate = categoryGstRateResolver.applyAsInt(productCategory);
        long taxAmount = Math.round((double) taxableAmount.getAmount() * gstRate / 100);

        log.debug("Tax calculation: taxableAmount={}, region={}, category={}, gstRate={}%, tax={}",
                taxableAmount, region, productCategory, gstRate, taxAmount);

        return Money.of(taxAmount, taxableAmount.getCurrency());
    }
}
