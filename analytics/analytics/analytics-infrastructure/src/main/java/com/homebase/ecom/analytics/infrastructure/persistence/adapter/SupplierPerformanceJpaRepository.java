package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.infrastructure.persistence.entity.SupplierPerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierPerformanceJpaRepository extends JpaRepository<SupplierPerformanceEntity, String> {

    Optional<SupplierPerformanceEntity> findBySupplierIdAndPeriodMonthAndPeriodYear(
            String supplierId, int periodMonth, int periodYear);
}
