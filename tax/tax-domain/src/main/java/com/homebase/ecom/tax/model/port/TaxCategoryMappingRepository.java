package com.homebase.ecom.tax.model.port;

import com.homebase.ecom.tax.model.TaxCategoryMapping;

import java.util.List;
import java.util.Optional;

public interface TaxCategoryMappingRepository {

    Optional<TaxCategoryMapping> findByCategoryId(String categoryId);

    Optional<TaxCategoryMapping> findByHsnCode(String hsnCode);

    List<TaxCategoryMapping> findAll();

    TaxCategoryMapping save(TaxCategoryMapping mapping);
}
