package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for UI Query module.
 *
 * Populates hm_query OLAP dimension/fact tables from OLTP source schemas.
 * These tables feed both Dashboard and Storefront query mappers.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:15 AM IST via @Scheduled.
 *
 * Tables synced:
 *   - hm_query.dim_product          (from products + categories + inventory + offers + reviews)
 *   - hm_query.dim_category         (from categories + products)
 *   - hm_query.dim_banner           (from banners)
 *   - hm_query.fact_inventory       (from inventory_item aggregated per product)
 *   - hm_query.cart_detail_flat     (from cart + cart_item + products + inventory)
 *   - hm_query.wishlist_flat        (from wishlist_items + user + products + offers)
 *   - hm_query.review_flat          (from product_reviews + user_profiles)
 *   - hm_query.notification_flat    (from customer_notifications)
 *   - hm_query.address_flat         (from user_address)
 *   - hm_query.return_detail_flat   (from returnrequest + order_items)
 *   - hm_query.audit_log_flat       (from audit_log)
 *   - hm_query.support_ticket_flat  (from support_tickets)
 *
 * MyBatis mapper: ui-query-sync.xml (namespace: UiQuerySync)
 */
public class UiQuerySyncService {

    private static final Logger log = LoggerFactory.getLogger(UiQuerySyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public UiQuerySyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync — runs nightly at 2:15 AM IST.
     * Truncates all OLAP tables and repopulates from OLTP.
     */
    @Scheduled(cron = "0 15 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting UI Query OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncDimProduct();
            syncDimCategory();
            syncDimBanner();
            syncFactInventory();
            syncCartDetailFlat();
            syncWishlistFlat();
            syncReviewFlat();
            syncNotificationFlat();
            syncAddressFlat();
            syncReturnDetailFlat();
            syncAuditLogFlat();
            syncSupportTicketFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("UI Query OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("UI Query OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimProduct() {
        log.info("Syncing dim_product...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateDimProduct");
            int rows = session.insert("UiQuerySync.syncDimProduct");
            session.commit();
            log.info("dim_product: {} rows synced", rows);
        }
    }

    public void syncDimCategory() {
        log.info("Syncing dim_category...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateDimCategory");
            int rows = session.insert("UiQuerySync.syncDimCategory");
            session.commit();
            log.info("dim_category: {} rows synced", rows);
        }
    }

    public void syncDimBanner() {
        log.info("Syncing dim_banner...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateDimBanner");
            int rows = session.insert("UiQuerySync.syncDimBanner");
            session.commit();
            log.info("dim_banner: {} rows synced", rows);
        }
    }

    public void syncFactInventory() {
        log.info("Syncing fact_inventory...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateFactInventory");
            int rows = session.insert("UiQuerySync.syncFactInventory");
            session.commit();
            log.info("fact_inventory: {} rows synced", rows);
        }
    }

    public void syncCartDetailFlat() {
        log.info("Syncing cart_detail_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateCartDetailFlat");
            int rows = session.insert("UiQuerySync.syncCartDetailFlat");
            session.commit();
            log.info("cart_detail_flat: {} rows synced", rows);
        }
    }

    public void syncWishlistFlat() {
        log.info("Syncing wishlist_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateWishlistFlat");
            int rows = session.insert("UiQuerySync.syncWishlistFlat");
            session.commit();
            log.info("wishlist_flat: {} rows synced", rows);
        }
    }

    public void syncReviewFlat() {
        log.info("Syncing review_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateReviewFlat");
            int rows = session.insert("UiQuerySync.syncReviewFlat");
            session.commit();
            log.info("review_flat: {} rows synced", rows);
        }
    }

    public void syncNotificationFlat() {
        log.info("Syncing notification_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateNotificationFlat");
            int rows = session.insert("UiQuerySync.syncNotificationFlat");
            session.commit();
            log.info("notification_flat: {} rows synced", rows);
        }
    }

    public void syncAddressFlat() {
        log.info("Syncing address_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateAddressFlat");
            int rows = session.insert("UiQuerySync.syncAddressFlat");
            session.commit();
            log.info("address_flat: {} rows synced", rows);
        }
    }

    public void syncReturnDetailFlat() {
        log.info("Syncing return_detail_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateReturnDetailFlat");
            int rows = session.insert("UiQuerySync.syncReturnDetailFlat");
            session.commit();
            log.info("return_detail_flat: {} rows synced", rows);
        }
    }

    public void syncAuditLogFlat() {
        log.info("Syncing audit_log_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateAuditLogFlat");
            int rows = session.insert("UiQuerySync.syncAuditLogFlat");
            session.commit();
            log.info("audit_log_flat: {} rows synced", rows);
        }
    }

    public void syncSupportTicketFlat() {
        log.info("Syncing support_ticket_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("UiQuerySync.truncateSupportTicketFlat");
            int rows = session.insert("UiQuerySync.syncSupportTicketFlat");
            session.commit();
            log.info("support_ticket_flat: {} rows synced", rows);
        }
    }
}
