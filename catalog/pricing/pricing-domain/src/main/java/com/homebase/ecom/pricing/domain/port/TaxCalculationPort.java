package com.homebase.ecom.pricing.domain.port;

import com.homebase.ecom.shared.Money;

/**
 * Port for calculating tax on a taxable amount.
 * Initially implemented inline (GST), later can call external tax engine.
 */
public interface TaxCalculationPort {
    Money calculateTax(Money taxableAmount, String region, String productCategory);
}
