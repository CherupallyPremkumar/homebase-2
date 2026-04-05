package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Seller Dashboard Query module.
 *
 * Populates hm_query.seller_dashboard_flat from OLTP supplier/order/review schemas.
 * This flat table feeds the seller-specific dashboard queries (KPIs, health score,
 * fulfillment rate, etc.).
 *
 * Other tables used by seller dashboard queries (order_details_flat, order_items_flat,
 * settlement_flat, product_catalog_flat, return_detail_flat) are synced by their
 * respective module sync services.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:35 AM IST via @Scheduled.
 *
 * MyBatis mapper: seller-dashboard-sync.xml (namespace: SellerDashboardSync)
 */
public class SellerDashboardSyncService {

    private static final Logger log = LoggerFactory.getLogger(SellerDashboardSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SellerDashboardSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync -- runs nightly at 2:35 AM IST.
     */
    @Scheduled(cron = "0 35 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting Seller Dashboard OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncSellerDashboardFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("Seller Dashboard OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("Seller Dashboard OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncSellerDashboardFlat() {
        log.info("Syncing seller_dashboard_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SellerDashboardSync.truncateSellerDashboardFlat");
            int rows = session.insert("SellerDashboardSync.syncSellerDashboardFlat");
            session.commit();
            log.info("seller_dashboard_flat: {} rows synced", rows);
        }
    }
}
