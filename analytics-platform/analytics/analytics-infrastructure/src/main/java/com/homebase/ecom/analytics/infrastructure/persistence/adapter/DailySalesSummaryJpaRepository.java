package com.homebase.ecom.analytics.infrastructure.persistence.adapter;

import com.homebase.ecom.analytics.infrastructure.persistence.entity.DailySalesSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySalesSummaryJpaRepository extends JpaRepository<DailySalesSummaryEntity, String> {

    Optional<DailySalesSummaryEntity> findBySummaryDate(LocalDate summaryDate);

    List<DailySalesSummaryEntity> findBySummaryDateBetweenOrderBySummaryDateAsc(LocalDate from, LocalDate to);
}
