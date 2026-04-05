package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates recommendation OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.recommendations_flat       (from product_recommendations + products)
 *   - hm_query.trending_products_flat      (from trending_products + products)
 *
 * Runs nightly at 3:35 AM IST.
 */
public class RecommendationSyncService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public RecommendationSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 35 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting recommendation OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncRecommendationsFlat();
            syncTrendingProductsFlat();
            log.info("Recommendation OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Recommendation OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncRecommendationsFlat() {
        log.info("Syncing recommendations_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RecommendationSync.truncateRecommendationsFlat");
            int rows = session.insert("RecommendationSync.syncRecommendationsFlat");
            session.commit();
            log.info("recommendations_flat: {} rows synced", rows);
        }
    }

    public void syncTrendingProductsFlat() {
        log.info("Syncing trending_products_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RecommendationSync.truncateTrendingProductsFlat");
            int rows = session.insert("RecommendationSync.syncTrendingProductsFlat");
            session.commit();
            log.info("trending_products_flat: {} rows synced", rows);
        }
    }
}
