package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Platform Dashboard Query module.
 *
 * Populates hm_query OLAP tables that feed the platform admin dashboard.
 * The computed aggregation tables (platform_kpi_snapshots, category_performance_daily,
 * seller_health_summary_daily, etc.) are populated by DashboardAnalyticsService.
 *
 * This service syncs the base flat tables that feed those computations:
 *   - hm_query.inventory_health_flat  (from inventory_item + products)
 *   - hm_query.product_catalog_flat   (from products + categories + offers + reviews + inventory)
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:10 AM IST via @Scheduled.
 *
 * MyBatis mapper: platform-dashboard-sync.xml (namespace: PlatformDashboardSync)
 */
public class PlatformDashboardSyncService {

    private static final Logger log = LoggerFactory.getLogger(PlatformDashboardSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public PlatformDashboardSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync -- runs nightly at 2:10 AM IST.
     */
    @Scheduled(cron = "0 10 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting Platform Dashboard OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncInventoryHealthFlat();
            syncProductCatalogFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("Platform Dashboard OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("Platform Dashboard OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncInventoryHealthFlat() {
        log.info("Syncing inventory_health_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PlatformDashboardSync.truncateInventoryHealthFlat");
            int rows = session.insert("PlatformDashboardSync.syncInventoryHealthFlat");
            session.commit();
            log.info("inventory_health_flat: {} rows synced", rows);
        }
    }

    public void syncProductCatalogFlat() {
        log.info("Syncing product_catalog_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PlatformDashboardSync.truncateProductCatalogFlat");
            int rows = session.insert("PlatformDashboardSync.syncProductCatalogFlat");
            session.commit();
            log.info("product_catalog_flat: {} rows synced", rows);
        }
    }
}
