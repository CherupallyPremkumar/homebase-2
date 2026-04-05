package com.homebase.ecom.query.service.analytics;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Map;

/**
 * Dashboard Analytics Processing Service.
 *
 * Reads from: hm_query.*_flat tables (denormalized data, synced by per-module ETL services)
 * Writes to:  hm_query.*_daily / *_snapshots / *_today tables (computed insights)
 *
 * NEVER reads from OLTP schemas. Only reads and writes within hm_query.
 *
 * Schedule: 3:00 AM IST — runs AFTER all flat table syncs complete (2:00-2:45 AM).
 *
 * Computations:
 *   1. platform_kpi_snapshots      — daily KPIs (GMV, orders, users, payment mix)
 *   2. category_performance_daily  — per-category metrics
 *   3. seller_health_summary_daily — seller health distribution
 *   4. conversion_funnel_daily     — session→view→cart→checkout→purchase
 *   5. revenue_by_region_daily     — per-state revenue
 *   6. top_products_today          — top 50 products by units sold
 *   7. top_sellers_today           — top 50 sellers by GMV
 *   8. live_order_pipeline         — order funnel counts with SLA breaches
 */
public class DashboardAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(DashboardAnalyticsService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public DashboardAnalyticsService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full analytics computation for yesterday's data.
     * Runs at 3:00 AM IST — after all flat table syncs complete.
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Kolkata")
    public void computeAll() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("Starting dashboard analytics computation for {}...", yesterday);
        long start = System.currentTimeMillis();

        try {
            // Daily aggregations (for yesterday)
            computePlatformKpiSnapshots(yesterday);
            computeCategoryPerformanceDaily(yesterday);
            computeSellerHealthSummaryDaily(yesterday);
            computeConversionFunnelDaily(yesterday);
            computeRevenueByRegionDaily(yesterday);

            // Real-time refreshes (for today)
            refreshTopProductsToday();
            refreshTopSellersToday();
            refreshLiveOrderPipeline();

            long duration = System.currentTimeMillis() - start;
            log.info("Dashboard analytics completed in {}ms for date {}", duration, yesterday);
        } catch (Exception e) {
            log.error("Dashboard analytics FAILED for date {}", yesterday, e);
            throw e;
        }
    }

    /**
     * Refresh only the real-time tables (top products, top sellers, pipeline).
     * Can be called more frequently — every 5 minutes for live dashboard.
     */
    @Scheduled(cron = "0 */5 6-23 * * *", zone = "Asia/Kolkata")
    public void refreshLiveTables() {
        log.debug("Refreshing live dashboard tables...");
        try {
            refreshTopProductsToday();
            refreshTopSellersToday();
            refreshLiveOrderPipeline();
        } catch (Exception e) {
            log.error("Live table refresh failed", e);
        }
    }

    public void computePlatformKpiSnapshots(LocalDate date) {
        log.info("Computing platform_kpi_snapshots for {}...", date);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("DashboardCompute.deletePlatformKpiForDate", Map.of("snapshotDate", date));
            int rows = session.insert("DashboardCompute.computePlatformKpiSnapshots", Map.of("snapshotDate", date));
            session.commit();
            log.info("platform_kpi_snapshots: {} rows computed for {}", rows, date);
        }
    }

    public void computeCategoryPerformanceDaily(LocalDate date) {
        log.info("Computing category_performance_daily for {}...", date);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("DashboardCompute.deleteCategoryPerformanceForDate", Map.of("snapshotDate", date));
            int rows = session.insert("DashboardCompute.computeCategoryPerformanceDaily", Map.of("snapshotDate", date));
            session.commit();
            log.info("category_performance_daily: {} rows computed for {}", rows, date);
        }
    }

    public void computeSellerHealthSummaryDaily(LocalDate date) {
        log.info("Computing seller_health_summary_daily for {}...", date);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("DashboardCompute.deleteSellerHealthForDate", Map.of("snapshotDate", date));
            int rows = session.insert("DashboardCompute.computeSellerHealthSummaryDaily", Map.of("snapshotDate", date));
            session.commit();
            log.info("seller_health_summary_daily: {} rows computed for {}", rows, date);
        }
    }

    public void computeConversionFunnelDaily(LocalDate date) {
        log.info("Computing conversion_funnel_daily for {}...", date);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("DashboardCompute.deleteConversionFunnelForDate", Map.of("snapshotDate", date));
            int rows = session.insert("DashboardCompute.computeConversionFunnelDaily", Map.of("snapshotDate", date));
            session.commit();
            log.info("conversion_funnel_daily: {} rows computed for {}", rows, date);
        }
    }

    public void computeRevenueByRegionDaily(LocalDate date) {
        log.info("Computing revenue_by_region_daily for {}...", date);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("DashboardCompute.deleteRevenueByRegionForDate", Map.of("snapshotDate", date));
            int rows = session.insert("DashboardCompute.computeRevenueByRegionDaily", Map.of("snapshotDate", date));
            session.commit();
            log.info("revenue_by_region_daily: {} rows computed for {}", rows, date);
        }
    }

    public void refreshTopProductsToday() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("DashboardCompute.truncateTopProductsToday");
            int rows = session.insert("DashboardCompute.computeTopProductsToday");
            session.commit();
            log.debug("top_products_today: {} rows refreshed", rows);
        }
    }

    public void refreshTopSellersToday() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("DashboardCompute.truncateTopSellersToday");
            int rows = session.insert("DashboardCompute.computeTopSellersToday");
            session.commit();
            log.debug("top_sellers_today: {} rows refreshed", rows);
        }
    }

    public void refreshLiveOrderPipeline() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("DashboardCompute.truncateLiveOrderPipeline");
            int rows = session.insert("DashboardCompute.computeLiveOrderPipeline");
            session.commit();
            log.debug("live_order_pipeline: {} rows refreshed", rows);
        }
    }
}
