package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates compliance OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.platform_policies_flat
 *   - hm_query.agreements_flat
 *   - hm_query.acceptances_flat
 *   - hm_query.regulations_flat
 *
 * Runs nightly at 3:05 AM IST.
 */
public class ComplianceSyncService {

    private static final Logger log = LoggerFactory.getLogger(ComplianceSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ComplianceSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 5 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting compliance OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncPlatformPoliciesFlat();
            syncAgreementsFlat();
            syncAcceptancesFlat();
            syncRegulationsFlat();
            log.info("Compliance OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Compliance OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncPlatformPoliciesFlat() {
        log.info("Syncing platform_policies_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ComplianceSync.truncatePlatformPoliciesFlat");
            int rows = session.insert("ComplianceSync.syncPlatformPoliciesFlat");
            session.commit();
            log.info("platform_policies_flat: {} rows synced", rows);
        }
    }

    public void syncAgreementsFlat() {
        log.info("Syncing agreements_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ComplianceSync.truncateAgreementsFlat");
            int rows = session.insert("ComplianceSync.syncAgreementsFlat");
            session.commit();
            log.info("agreements_flat: {} rows synced", rows);
        }
    }

    public void syncAcceptancesFlat() {
        log.info("Syncing acceptances_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ComplianceSync.truncateAcceptancesFlat");
            int rows = session.insert("ComplianceSync.syncAcceptancesFlat");
            session.commit();
            log.info("acceptances_flat: {} rows synced", rows);
        }
    }

    public void syncRegulationsFlat() {
        log.info("Syncing regulations_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ComplianceSync.truncateRegulationsFlat");
            int rows = session.insert("ComplianceSync.syncRegulationsFlat");
            session.commit();
            log.info("regulations_flat: {} rows synced", rows);
        }
    }
}
