package com.homebase.ecom.tax.model.port;

import com.homebase.ecom.tax.model.TaxExemption;
import java.util.List;

public interface TaxExemptionRepository {
    List<TaxExemption> findActiveExemptions();
    boolean isExempt(String exemptionType, String value);
}
