package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates wishlist OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.wishlist_flat  (wishlist items + product + variant + inventory enrichment)
 *
 * Runs nightly at 2:15 AM IST.
 */
public class WishlistSyncService {

    private static final Logger log = LoggerFactory.getLogger(WishlistSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public WishlistSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 15 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting wishlist OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncWishlistFlat();
            log.info("Wishlist OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Wishlist OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncWishlistFlat() {
        log.info("Syncing wishlist_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WishlistSync.truncateWishlistFlat");
            int rows = session.insert("WishlistSync.syncWishlistFlat");
            session.commit();
            log.info("wishlist_flat: {} rows synced", rows);
        }
    }
}
