package com.homebase.ecom.tax.model.port;

import com.homebase.ecom.tax.model.TaxRate;

import java.util.List;
import java.util.Optional;

public interface TaxRateRepository {

    Optional<TaxRate> findById(String id);
    Optional<TaxRate> findByHsnCode(String hsnCode);
    Optional<TaxRate> findActiveByHsnCode(String hsnCode);
    List<TaxRate> findByRegionCode(String regionCode);
    TaxRate save(TaxRate taxRate);
    void delete(String id);
}
