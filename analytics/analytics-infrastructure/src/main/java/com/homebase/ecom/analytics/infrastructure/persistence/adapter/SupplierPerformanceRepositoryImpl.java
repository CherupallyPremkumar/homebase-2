package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.model.SupplierPerformance;
import com.homebase.ecom.analytics.port.SupplierPerformanceRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.mapper.AnalyticsMapper;

import java.util.Optional;

public class SupplierPerformanceRepositoryImpl implements SupplierPerformanceRepository {

    private final SupplierPerformanceJpaRepository jpaRepository;
    private final AnalyticsMapper mapper;

    public SupplierPerformanceRepositoryImpl(SupplierPerformanceJpaRepository jpaRepository, AnalyticsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SupplierPerformance> findBySupplierIdAndPeriod(String supplierId, int month, int year) {
        return jpaRepository.findBySupplierIdAndPeriodMonthAndPeriodYear(supplierId, month, year)
                .map(mapper::toModel);
    }

    @Override
    public void save(SupplierPerformance performance) {
        jpaRepository.save(mapper.toEntity(performance));
    }
}
