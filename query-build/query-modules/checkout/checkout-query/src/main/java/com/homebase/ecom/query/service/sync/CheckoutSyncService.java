package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates checkout OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.checkout_flat  (checkout + customer + cart item count)
 *
 * Runs nightly at 2:05 AM IST.
 */
public class CheckoutSyncService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public CheckoutSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 5 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting checkout OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncCheckoutFlat();
            log.info("Checkout OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Checkout OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCheckoutFlat() {
        log.info("Syncing checkout_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CheckoutSync.truncateCheckoutFlat");
            int rows = session.insert("CheckoutSync.syncCheckoutFlat");
            session.commit();
            log.info("checkout_flat: {} rows synced", rows);
        }
    }
}
