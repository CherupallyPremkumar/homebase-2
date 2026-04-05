package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.media_flat from OLTP.
 *
 * Source: hm_cms.cms_media
 * Target: hm_query.media_flat
 *
 * Runs nightly at 3:55 AM IST.
 */
public class MediaSyncService {

    private static final Logger log = LoggerFactory.getLogger(MediaSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public MediaSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 55 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting media OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncMediaFlat();
            log.info("Media OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Media OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncMediaFlat() {
        log.info("Syncing media_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("MediaSync.truncateMediaFlat");
            int rows = session.insert("MediaSync.syncMediaFlat");
            session.commit();
            log.info("media_flat: {} rows synced", rows);
        }
    }
}
