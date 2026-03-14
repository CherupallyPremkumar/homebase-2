package com.homebase.ecom.analytics.port;

import com.homebase.ecom.analytics.model.ProductPerformance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductPerformanceRepository {

    Optional<ProductPerformance> findByProductIdAndDate(String productId, LocalDate date);

    List<ProductPerformance> findByProductIdAndDateRange(String productId, LocalDate from, LocalDate to);

    void save(ProductPerformance performance);
}
