package com.homebase.ecom.tax.service;

import com.homebase.ecom.tax.dto.TaxCalculationRequestDTO;
import com.homebase.ecom.tax.dto.TaxCalculationResponseDTO;
import com.homebase.ecom.tax.dto.TaxRateDto;

import java.util.List;

public interface TaxService {

    /**
     * Stateless tax calculation for a set of items.
     * Determines IGST vs CGST+SGST based on source/destination state.
     */
    TaxCalculationResponseDTO calculateTax(TaxCalculationRequestDTO request);

    /**
     * Lookup tax rates applied to an existing order (historical).
     */
    List<TaxRateDto> getOrderTaxLines(String orderId);

    TaxRateDto getTaxRate(String regionCode, String categoryId);
}
