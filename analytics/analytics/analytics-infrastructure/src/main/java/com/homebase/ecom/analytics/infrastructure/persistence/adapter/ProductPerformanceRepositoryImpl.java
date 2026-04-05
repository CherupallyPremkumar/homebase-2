package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.model.ProductPerformance;
import com.homebase.ecom.analytics.port.ProductPerformanceRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.mapper.AnalyticsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductPerformanceRepositoryImpl implements ProductPerformanceRepository {

    private final ProductPerformanceJpaRepository jpaRepository;
    private final AnalyticsMapper mapper;

    public ProductPerformanceRepositoryImpl(ProductPerformanceJpaRepository jpaRepository, AnalyticsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductPerformance> findByProductIdAndDate(String productId, LocalDate date) {
        return jpaRepository.findByProductIdAndPeriodDate(productId, date).map(mapper::toModel);
    }

    @Override
    public List<ProductPerformance> findByProductIdAndDateRange(String productId, LocalDate from, LocalDate to) {
        return jpaRepository.findByProductIdAndPeriodDateBetweenOrderByPeriodDateAsc(productId, from, to)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductPerformance performance) {
        jpaRepository.save(mapper.toEntity(performance));
    }
}
