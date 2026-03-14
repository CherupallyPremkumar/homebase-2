package com.homebase.ecom.analytics.port;

import com.homebase.ecom.analytics.model.DailySalesSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySalesSummaryRepository {

    Optional<DailySalesSummary> findByDate(LocalDate date);

    List<DailySalesSummary> findByDateRange(LocalDate from, LocalDate to);

    void save(DailySalesSummary summary);
}
