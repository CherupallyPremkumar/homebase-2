package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates analytics OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.platform_kpi_snapshots   (from daily_sales_summary)
 *   - hm_query.product_performance_flat  (from product_performance + products)
 *
 * Runs nightly at 3:15 AM IST.
 */
public class AnalyticsSyncService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public AnalyticsSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 15 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting analytics OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncPlatformKpiSnapshots();
            syncProductPerformanceFlat();
            log.info("Analytics OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Analytics OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncPlatformKpiSnapshots() {
        log.info("Syncing platform_kpi_snapshots...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("AnalyticsSync.truncatePlatformKpiSnapshots");
            int rows = session.insert("AnalyticsSync.syncPlatformKpiSnapshots");
            session.commit();
            log.info("platform_kpi_snapshots: {} rows synced", rows);
        }
    }

    public void syncProductPerformanceFlat() {
        log.info("Syncing product_performance_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("AnalyticsSync.truncateProductPerformanceFlat");
            int rows = session.insert("AnalyticsSync.syncProductPerformanceFlat");
            session.commit();
            log.info("product_performance_flat: {} rows synced", rows);
        }
    }
}
