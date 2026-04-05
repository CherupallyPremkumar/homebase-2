package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates dispute OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.disputes_flat  (dispute + orders + user_profiles + supplier)
 *
 * Runs nightly at 3:25 AM IST.
 */
public class DisputeSyncService {

    private static final Logger log = LoggerFactory.getLogger(DisputeSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public DisputeSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 25 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting dispute OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDisputesFlat();
            log.info("Dispute OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Dispute OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDisputesFlat() {
        log.info("Syncing disputes_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("DisputesSync.truncateDisputesFlat");
            int rows = session.insert("DisputesSync.syncDisputesFlat");
            session.commit();
            log.info("disputes_flat: {} rows synced", rows);
        }
    }
}
