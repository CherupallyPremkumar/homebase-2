package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.cconfig_flat from OLTP.
 *
 * Source: cconfig (shared schema)
 * Target: hm_query.cconfig_flat
 *
 * Runs nightly at 3:45 AM IST.
 */
public class CconfigSyncService {

    private static final Logger log = LoggerFactory.getLogger(CconfigSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public CconfigSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 45 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting cconfig OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncCconfigFlat();
            log.info("Cconfig OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Cconfig OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCconfigFlat() {
        log.info("Syncing cconfig_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CconfigSync.truncateCconfigFlat");
            int rows = session.insert("CconfigSync.syncCconfigFlat");
            session.commit();
            log.info("cconfig_flat: {} rows synced", rows);
        }
    }
}
