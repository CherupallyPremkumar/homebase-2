package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.fact_reviews_flat from OLTP.
 *
 * Source: product_reviews JOIN products JOIN user_profiles
 * Target: hm_query.fact_reviews_flat
 *
 * Runs nightly at 2:30 AM IST.
 */
public class ReviewSyncService {

    private static final Logger log = LoggerFactory.getLogger(ReviewSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ReviewSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting review OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncFactReviewsFlat();
            log.info("Review OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Review OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncFactReviewsFlat() {
        log.info("Syncing fact_reviews_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ReviewSync.truncateFactReviewsFlat");
            int rows = session.insert("ReviewSync.syncFactReviewsFlat");
            session.commit();
            log.info("fact_reviews_flat: {} rows synced", rows);
        }
    }
}
