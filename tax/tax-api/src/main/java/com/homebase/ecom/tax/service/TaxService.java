package com.homebase.ecom.tax.service;

import com.homebase.ecom.tax.dto.TaxRateDto;

import java.util.List;

public interface TaxService {

    List<TaxRateDto> calculateTax(String orderId);

    TaxRateDto getTaxRate(String regionCode, String categoryId);
}
