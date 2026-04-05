package com.homebase.ecom.analytics.service.configuration;

import com.homebase.ecom.analytics.port.DailySalesSummaryRepository;
import com.homebase.ecom.analytics.port.ProductPerformanceRepository;
import com.homebase.ecom.analytics.port.SupplierPerformanceRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.DailySalesSummaryJpaRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.DailySalesSummaryRepositoryImpl;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.ProductPerformanceJpaRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.ProductPerformanceRepositoryImpl;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.SupplierPerformanceJpaRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.adapter.SupplierPerformanceRepositoryImpl;
import com.homebase.ecom.analytics.infrastructure.persistence.mapper.AnalyticsMapper;
import com.homebase.ecom.analytics.service.AnalyticsService;
import com.homebase.ecom.analytics.service.impl.AnalyticsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsConfiguration {

    @Bean
    public AnalyticsMapper analyticsMapper() {
        return new AnalyticsMapper();
    }

    @Bean
    public DailySalesSummaryRepository dailySalesSummaryRepository(
            DailySalesSummaryJpaRepository jpaRepository, AnalyticsMapper mapper) {
        return new DailySalesSummaryRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public ProductPerformanceRepository productPerformanceRepository(
            ProductPerformanceJpaRepository jpaRepository, AnalyticsMapper mapper) {
        return new ProductPerformanceRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public SupplierPerformanceRepository supplierPerformanceRepository(
            SupplierPerformanceJpaRepository jpaRepository, AnalyticsMapper mapper) {
        return new SupplierPerformanceRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public AnalyticsService analyticsService(
            DailySalesSummaryRepository dailySalesRepo,
            ProductPerformanceRepository productPerfRepo,
            SupplierPerformanceRepository supplierPerfRepo,
            AnalyticsMapper mapper) {
        return new AnalyticsServiceImpl(dailySalesRepo, productPerfRepo, supplierPerfRepo, mapper);
    }
}
