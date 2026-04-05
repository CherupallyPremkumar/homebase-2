package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates settlement OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.fact_settlements  (settlements)
 *   - hm_query.dim_seller        (supplier — upsert)
 *
 * Read queries JOIN fact_settlements with dim_seller for star-schema reads.
 *
 * Runs nightly at 3:10 AM IST.
 */
public class SettlementSyncService {

    private static final Logger log = LoggerFactory.getLogger(SettlementSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SettlementSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 10 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting settlement OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimSeller();
            syncFactSettlements();
            log.info("Settlement OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Settlement OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimSeller() {
        log.info("Syncing dim_seller for settlements...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("SettlementSync.syncDimSeller");
            session.commit();
            log.info("dim_seller (settlements): {} rows upserted", rows);
        }
    }

    public void syncFactSettlements() {
        log.info("Syncing fact_settlements...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SettlementSync.truncateFactSettlements");
            int rows = session.insert("SettlementSync.syncFactSettlements");
            session.commit();
            log.info("fact_settlements: {} rows synced", rows);
        }
    }
}
