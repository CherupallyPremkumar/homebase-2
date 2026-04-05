package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates return request OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.fact_returns   (returnrequest + orders)
 *   - hm_query.dim_customer   (user_profiles — upsert)
 *   - hm_query.dim_seller     (supplier — upsert)
 *   - hm_query.dim_product    (products — upsert)
 *
 * Read queries JOIN fact_returns with dim_customer, dim_seller,
 * and dim_product for denormalized star-schema reads.
 *
 * Runs nightly at 3:00 AM IST.
 */
public class ReturnRequestSyncService {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ReturnRequestSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting return request OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimCustomer();
            syncDimSeller();
            syncDimProduct();
            syncFactReturns();
            log.info("Return request OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Return request OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimCustomer() {
        log.info("Syncing dim_customer for returns...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("ReturnrequestSync.syncDimCustomer");
            session.commit();
            log.info("dim_customer (returns): {} rows upserted", rows);
        }
    }

    public void syncDimSeller() {
        log.info("Syncing dim_seller for returns...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("ReturnrequestSync.syncDimSeller");
            session.commit();
            log.info("dim_seller (returns): {} rows upserted", rows);
        }
    }

    public void syncDimProduct() {
        log.info("Syncing dim_product for returns...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("ReturnrequestSync.syncDimProduct");
            session.commit();
            log.info("dim_product (returns): {} rows upserted", rows);
        }
    }

    public void syncFactReturns() {
        log.info("Syncing fact_returns...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReturnrequestSync.truncateFactReturns");
            int rows = session.insert("ReturnrequestSync.syncFactReturns");
            session.commit();
            log.info("fact_returns: {} rows synced", rows);
        }
    }
}
