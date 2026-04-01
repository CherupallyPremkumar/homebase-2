package com.homebase.ecom.tax.model.port;

import com.homebase.ecom.tax.model.TaxRegion;
import java.util.Optional;

public interface TaxRegionRepository {
    Optional<TaxRegion> findByStateCode(String stateCode);
    Optional<TaxRegion> findByStateAlpha(String stateAlpha);
}
