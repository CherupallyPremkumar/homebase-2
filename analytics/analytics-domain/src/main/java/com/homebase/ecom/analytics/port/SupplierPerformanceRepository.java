package com.homebase.ecom.analytics.port;

import com.homebase.ecom.analytics.model.SupplierPerformance;

import java.util.Optional;

public interface SupplierPerformanceRepository {

    Optional<SupplierPerformance> findBySupplierIdAndPeriod(String supplierId, int month, int year);

    void save(SupplierPerformance performance);
}
