package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.model.DailySalesSummary;
import com.homebase.ecom.analytics.port.DailySalesSummaryRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.mapper.AnalyticsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DailySalesSummaryRepositoryImpl implements DailySalesSummaryRepository {

    private final DailySalesSummaryJpaRepository jpaRepository;
    private final AnalyticsMapper mapper;

    public DailySalesSummaryRepositoryImpl(DailySalesSummaryJpaRepository jpaRepository, AnalyticsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<DailySalesSummary> findByDate(LocalDate date) {
        return jpaRepository.findBySummaryDate(date).map(mapper::toModel);
    }

    @Override
    public List<DailySalesSummary> findByDateRange(LocalDate from, LocalDate to) {
        return jpaRepository.findBySummaryDateBetweenOrderBySummaryDateAsc(from, to)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void save(DailySalesSummary summary) {
        jpaRepository.save(mapper.toEntity(summary));
    }
}
