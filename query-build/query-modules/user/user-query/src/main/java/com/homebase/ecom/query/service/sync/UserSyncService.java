package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates user/customer OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.dim_customer       (star schema dimension — listing)
 *   - hm_query.customer_360_flat  (denormalized flat — detail page)
 *
 * Runs nightly at 2:20 AM IST.
 */
public class UserSyncService {

    private static final Logger log = LoggerFactory.getLogger(UserSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public UserSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 20 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting user OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimCustomer();
            syncCustomer360Flat();
            log.info("User OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("User OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimCustomer() {
        log.info("Syncing dim_customer...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UserSync.truncateDimCustomer");
            int rows = session.insert("UserSync.syncDimCustomer");
            session.commit();
            log.info("dim_customer: {} rows synced", rows);
        }
    }

    public void syncCustomer360Flat() {
        log.info("Syncing customer_360_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UserSync.truncateCustomer360Flat");
            int rows = session.insert("UserSync.syncCustomer360Flat");
            session.commit();
            log.info("customer_360_flat: {} rows synced", rows);
        }
    }
}
