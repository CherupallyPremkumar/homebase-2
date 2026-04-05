package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates return processing OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.return_processing_flat  (return_processing_saga + returnrequest + user_profiles + warehouses)
 *
 * Runs nightly at 3:05 AM IST.
 */
public class ReturnProcessingSyncService {

    private static final Logger log = LoggerFactory.getLogger(ReturnProcessingSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ReturnProcessingSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 5 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting return processing OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncReturnProcessingFlat();
            log.info("Return processing OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Return processing OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncReturnProcessingFlat() {
        log.info("Syncing return_processing_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReturnProcessingSync.truncateReturnProcessingFlat");
            int rows = session.insert("ReturnProcessingSync.syncReturnProcessingFlat");
            session.commit();
            log.info("return_processing_flat: {} rows synced", rows);
        }
    }
}
