package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.organisation_flat from OLTP.
 *
 * Source: organisation + organisation_branding + organisation_contact
 *         + organisation_address + organisation_locale + organisation_social
 * Target: hm_query.organisation_flat
 *
 * Runs nightly at 3:50 AM IST.
 */
public class OrganisationSyncService {

    private static final Logger log = LoggerFactory.getLogger(OrganisationSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public OrganisationSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 50 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting organisation OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncOrganisationFlat();
            log.info("Organisation OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Organisation OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncOrganisationFlat() {
        log.info("Syncing organisation_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrganisationSync.truncateOrganisationFlat");
            int rows = session.insert("OrganisationSync.syncOrganisationFlat");
            session.commit();
            log.info("organisation_flat: {} rows synced", rows);
        }
    }
}
