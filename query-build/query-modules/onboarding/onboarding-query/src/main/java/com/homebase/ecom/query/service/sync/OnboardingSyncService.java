package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates onboarding OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.onboarding_flat  (supplier onboarding denormalized)
 *
 * Runs nightly at 2:30 AM IST.
 */
public class OnboardingSyncService {

    private static final Logger log = LoggerFactory.getLogger(OnboardingSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public OnboardingSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting onboarding OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncOnboardingFlat();
            log.info("Onboarding OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Onboarding OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncOnboardingFlat() {
        log.info("Syncing onboarding_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OnboardingSync.truncateOnboardingFlat");
            int rows = session.insert("OnboardingSync.syncOnboardingFlat");
            session.commit();
            log.info("onboarding_flat: {} rows synced", rows);
        }
    }
}
