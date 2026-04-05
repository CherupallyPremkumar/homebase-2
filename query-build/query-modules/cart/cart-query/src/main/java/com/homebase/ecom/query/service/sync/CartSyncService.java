package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates ALL cart OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.cart_flat           (cart + customer denormalized)
 *   - hm_query.cart_items_flat     (cart items + product + variant + inventory enrichment)
 *   - hm_query.cart_activity_flat  (audit trail — last 90 days)
 *
 * Runs nightly at 2:00 AM IST.
 */
public class CartSyncService {

    private static final Logger log = LoggerFactory.getLogger(CartSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public CartSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting cart OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncCartFlat();
            syncCartItemsFlat();
            syncCartActivityFlat();
            log.info("Cart OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Cart OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCartFlat() {
        log.info("Syncing cart_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CartSync.truncateCartFlat");
            int rows = session.insert("CartSync.syncCartFlat");
            session.commit();
            log.info("cart_flat: {} rows synced", rows);
        }
    }

    public void syncCartItemsFlat() {
        log.info("Syncing cart_items_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CartSync.truncateCartItemsFlat");
            int rows = session.insert("CartSync.syncCartItemsFlat");
            session.commit();
            log.info("cart_items_flat: {} rows synced", rows);
        }
    }

    public void syncCartActivityFlat() {
        log.info("Syncing cart_activity_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CartSync.truncateCartActivityFlat");
            int rows = session.insert("CartSync.syncCartActivityFlat");
            session.commit();
            log.info("cart_activity_flat: {} rows synced", rows);
        }
    }
}
