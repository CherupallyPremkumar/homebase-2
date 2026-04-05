package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates clickstream OLAP tables from OLTP.
 *
 * Tables synced (last 90 days only):
 *   - hm_query.page_views_flat
 *   - hm_query.click_events_flat
 *
 * Runs nightly at 3:20 AM IST.
 */
public class ClickstreamSyncService {

    private static final Logger log = LoggerFactory.getLogger(ClickstreamSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ClickstreamSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 20 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting clickstream OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncPageViewsFlat();
            syncClickEventsFlat();
            log.info("Clickstream OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Clickstream OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncPageViewsFlat() {
        log.info("Syncing page_views_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ClickstreamSync.truncatePageViewsFlat");
            int rows = session.insert("ClickstreamSync.syncPageViewsFlat");
            session.commit();
            log.info("page_views_flat: {} rows synced", rows);
        }
    }

    public void syncClickEventsFlat() {
        log.info("Syncing click_events_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ClickstreamSync.truncateClickEventsFlat");
            int rows = session.insert("ClickstreamSync.syncClickEventsFlat");
            session.commit();
            log.info("click_events_flat: {} rows synced", rows);
        }
    }
}
