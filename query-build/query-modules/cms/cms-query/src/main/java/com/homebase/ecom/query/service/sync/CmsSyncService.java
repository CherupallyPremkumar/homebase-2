package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates all CMS OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.cms_pages_flat
 *   - hm_query.cms_banners_flat
 *   - hm_query.cms_blocks_flat
 *   - hm_query.cms_media_flat
 *   - hm_query.cms_page_versions_flat
 *   - hm_query.cms_schedules_flat
 *   - hm_query.cms_announcements_flat
 *   - hm_query.cms_seo_meta_flat
 *
 * Runs nightly at 3:00 AM IST.
 */
public class CmsSyncService {

    private static final Logger log = LoggerFactory.getLogger(CmsSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public CmsSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting CMS OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncCmsPagesFlat();
            syncCmsBannersFlat();
            syncCmsBlocksFlat();
            syncCmsMediaFlat();
            syncCmsPageVersionsFlat();
            syncCmsSchedulesFlat();
            syncCmsAnnouncementsFlat();
            syncCmsSeoMetaFlat();
            log.info("CMS OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("CMS OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCmsPagesFlat() {
        log.info("Syncing cms_pages_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsPagesFlat");
            int rows = session.insert("CmsSync.syncCmsPagesFlat");
            session.commit();
            log.info("cms_pages_flat: {} rows synced", rows);
        }
    }

    public void syncCmsBannersFlat() {
        log.info("Syncing cms_banners_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsBannersFlat");
            int rows = session.insert("CmsSync.syncCmsBannersFlat");
            session.commit();
            log.info("cms_banners_flat: {} rows synced", rows);
        }
    }

    public void syncCmsBlocksFlat() {
        log.info("Syncing cms_blocks_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsBlocksFlat");
            int rows = session.insert("CmsSync.syncCmsBlocksFlat");
            session.commit();
            log.info("cms_blocks_flat: {} rows synced", rows);
        }
    }

    public void syncCmsMediaFlat() {
        log.info("Syncing cms_media_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsMediaFlat");
            int rows = session.insert("CmsSync.syncCmsMediaFlat");
            session.commit();
            log.info("cms_media_flat: {} rows synced", rows);
        }
    }

    public void syncCmsPageVersionsFlat() {
        log.info("Syncing cms_page_versions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsPageVersionsFlat");
            int rows = session.insert("CmsSync.syncCmsPageVersionsFlat");
            session.commit();
            log.info("cms_page_versions_flat: {} rows synced", rows);
        }
    }

    public void syncCmsSchedulesFlat() {
        log.info("Syncing cms_schedules_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsSchedulesFlat");
            int rows = session.insert("CmsSync.syncCmsSchedulesFlat");
            session.commit();
            log.info("cms_schedules_flat: {} rows synced", rows);
        }
    }

    public void syncCmsAnnouncementsFlat() {
        log.info("Syncing cms_announcements_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsAnnouncementsFlat");
            int rows = session.insert("CmsSync.syncCmsAnnouncementsFlat");
            session.commit();
            log.info("cms_announcements_flat: {} rows synced", rows);
        }
    }

    public void syncCmsSeoMetaFlat() {
        log.info("Syncing cms_seo_meta_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CmsSync.truncateCmsSeoMetaFlat");
            int rows = session.insert("CmsSync.syncCmsSeoMetaFlat");
            session.commit();
            log.info("cms_seo_meta_flat: {} rows synced", rows);
        }
    }
}
