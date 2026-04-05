package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates reporting OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.report_definitions_flat
 *   - hm_query.scheduled_reports_flat
 *   - hm_query.report_history_flat
 *
 * Runs nightly at 3:30 AM IST.
 */
public class ReportingSyncService {

    private static final Logger log = LoggerFactory.getLogger(ReportingSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ReportingSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 30 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting reporting OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncReportDefinitionsFlat();
            syncScheduledReportsFlat();
            syncReportHistoryFlat();
            log.info("Reporting OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Reporting OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncReportDefinitionsFlat() {
        log.info("Syncing report_definitions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReportingSync.truncateReportDefinitionsFlat");
            int rows = session.insert("ReportingSync.syncReportDefinitionsFlat");
            session.commit();
            log.info("report_definitions_flat: {} rows synced", rows);
        }
    }

    public void syncScheduledReportsFlat() {
        log.info("Syncing scheduled_reports_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReportingSync.truncateScheduledReportsFlat");
            int rows = session.insert("ReportingSync.syncScheduledReportsFlat");
            session.commit();
            log.info("scheduled_reports_flat: {} rows synced", rows);
        }
    }

    public void syncReportHistoryFlat() {
        log.info("Syncing report_history_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReportingSync.truncateReportHistoryFlat");
            int rows = session.insert("ReportingSync.syncReportHistoryFlat");
            session.commit();
            log.info("report_history_flat: {} rows synced", rows);
        }
    }
}
