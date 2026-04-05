package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.infrastructure.persistence.entity.ProductPerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductPerformanceJpaRepository extends JpaRepository<ProductPerformanceEntity, String> {

    Optional<ProductPerformanceEntity> findByProductIdAndPeriodDate(String productId, LocalDate periodDate);

    List<ProductPerformanceEntity> findByProductIdAndPeriodDateBetweenOrderByPeriodDateAsc(
            String productId, LocalDate from, LocalDate to);
}
