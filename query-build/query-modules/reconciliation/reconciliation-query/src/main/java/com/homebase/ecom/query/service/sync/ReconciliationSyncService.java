package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.reconciliation_batches_flat from OLTP.
 *
 * Source: reconciliation_batches
 * Target: hm_query.reconciliation_batches_flat
 *
 * Runs nightly at 3:25 AM IST.
 */
public class ReconciliationSyncService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ReconciliationSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 25 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting reconciliation OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncReconciliationBatchesFlat();
            log.info("Reconciliation OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Reconciliation OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncReconciliationBatchesFlat() {
        log.info("Syncing reconciliation_batches_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReconciliationSync.truncateReconciliationBatchesFlat");
            int rows = session.insert("ReconciliationSync.syncReconciliationBatchesFlat");
            session.commit();
            log.info("reconciliation_batches_flat: {} rows synced", rows);
        }
    }
}
