package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.notification_flat from OLTP.
 *
 * Source: notifications JOIN user_profiles
 * Target: hm_query.notification_flat
 *
 * Runs nightly at 2:35 AM IST.
 */
public class NotificationSyncService {

    private static final Logger log = LoggerFactory.getLogger(NotificationSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public NotificationSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 35 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting notification OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncNotificationFlat();
            log.info("Notification OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Notification OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncNotificationFlat() {
        log.info("Syncing notification_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("NotificationSync.truncateNotificationFlat");
            int rows = session.insert("NotificationSync.syncNotificationFlat");
            session.commit();
            log.info("notification_flat: {} rows synced", rows);
        }
    }
}
