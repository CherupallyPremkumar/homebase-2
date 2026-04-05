package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — syncs seller-specific OLAP data from OLTP.
 *
 * Tables synced:
 *   - hm_query.seller_dashboard_flat  (store-level seller rows + performance)
 *
 * Note: The main seller_dashboard_flat is populated by SupplierSyncService
 * from hm_supplier.supplier. This service handles:
 *   1. Sellers that exist in hm_seller.sellers but not in hm_supplier.supplier
 *   2. Performance metrics from hm_seller_lifecycle
 *
 * Runs nightly at 2:35 AM IST (after SupplierSyncService at 2:25 AM).
 */
public class SellerSyncService {

    private static final Logger log = LoggerFactory.getLogger(SellerSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SellerSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 35 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting seller OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncSellerFlat();
            syncSellerPerformance();
            log.info("Seller OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Seller OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncSellerFlat() {
        log.info("Syncing seller rows into seller_dashboard_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SellerSync.truncateSellerFlat");
            int rows = session.insert("SellerSync.syncSellerFlat");
            session.commit();
            log.info("seller_dashboard_flat (seller rows): {} rows synced", rows);
        }
    }

    public void syncSellerPerformance() {
        log.info("Syncing seller performance metrics...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("SellerSync.syncSellerPerformance");
            session.commit();
            log.info("seller_dashboard_flat (performance): {} rows updated", rows);
        }
    }
}
