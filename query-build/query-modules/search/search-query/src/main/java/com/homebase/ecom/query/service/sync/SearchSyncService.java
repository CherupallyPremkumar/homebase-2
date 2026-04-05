package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Search Query module.
 *
 * The core dim_product and dim_category tables (used by search queries)
 * are synced by UiQuerySyncService and ProductCatalogSyncService.
 * This service syncs search-specific OLAP tables only.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:25 AM IST via @Scheduled.
 *
 * Tables synced:
 *   - hm_query.search_popularity_flat  (from search_log — top 10k terms)
 *
 * MyBatis mapper: search-sync.xml (namespace: SearchSync)
 */
public class SearchSyncService {

    private static final Logger log = LoggerFactory.getLogger(SearchSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SearchSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync — runs nightly at 2:25 AM IST.
     */
    @Scheduled(cron = "0 25 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting Search OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncSearchPopularityFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("Search OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("Search OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncSearchPopularityFlat() {
        log.info("Syncing search_popularity_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("SearchSync.truncateSearchPopularityFlat");
            int rows = session.insert("SearchSync.syncSearchPopularityFlat");
            session.commit();
            log.info("search_popularity_flat: {} rows synced", rows);
        }
    }
}
