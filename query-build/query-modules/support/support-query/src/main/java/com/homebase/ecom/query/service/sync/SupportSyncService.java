package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.fact_support_tickets_flat from OLTP.
 *
 * Source: support_tickets JOIN user_profiles
 * Target: hm_query.fact_support_tickets_flat
 *
 * Runs nightly at 2:40 AM IST.
 */
public class SupportSyncService {

    private static final Logger log = LoggerFactory.getLogger(SupportSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SupportSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 40 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting support OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncFactSupportTicketsFlat();
            log.info("Support OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Support OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncFactSupportTicketsFlat() {
        log.info("Syncing fact_support_tickets_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SupportSync.truncateFactSupportTicketsFlat");
            int rows = session.insert("SupportSync.syncFactSupportTicketsFlat");
            session.commit();
            log.info("fact_support_tickets_flat: {} rows synced", rows);
        }
    }
}
