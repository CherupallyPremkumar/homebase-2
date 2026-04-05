package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates supplier OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.dim_seller             (star schema dimension — listing)
 *   - hm_query.seller_dashboard_flat   (denormalized flat — detail page)
 *
 * Runs nightly at 2:25 AM IST.
 */
public class SupplierSyncService {

    private static final Logger log = LoggerFactory.getLogger(SupplierSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SupplierSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 25 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting supplier OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimSeller();
            syncSellerDashboardFlat();
            log.info("Supplier OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Supplier OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimSeller() {
        log.info("Syncing dim_seller...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SupplierSync.truncateDimSeller");
            int rows = session.insert("SupplierSync.syncDimSeller");
            session.commit();
            log.info("dim_seller: {} rows synced", rows);
        }
    }

    public void syncSellerDashboardFlat() {
        log.info("Syncing seller_dashboard_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SupplierSync.truncateSellerDashboardFlat");
            int rows = session.insert("SupplierSync.syncSellerDashboardFlat");
            session.commit();
            log.info("seller_dashboard_flat: {} rows synced", rows);
        }
    }
}
