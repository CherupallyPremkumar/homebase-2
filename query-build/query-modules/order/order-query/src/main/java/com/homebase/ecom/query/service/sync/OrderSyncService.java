package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates ALL order OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.order_details_flat      (orders + items + customer + seller + payment + shipping + address)
 *   - hm_query.order_items_flat        (order line items — one-to-many)
 *   - hm_query.order_activity_flat     (audit trail — last 90 days)
 *   - hm_query.shipment_tracking_flat  (tracking events — last 90 days)
 *   - hm_query.order_risk_flat         (fraud assessments per order)
 *
 * Runs nightly at 2:45 AM IST.
 */
public class OrderSyncService {

    private static final Logger log = LoggerFactory.getLogger(OrderSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public OrderSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 45 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting order OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncOrderDetailsFlat();
            syncOrderItemsFlat();
            syncOrderActivityFlat();
            syncShipmentTrackingFlat();
            syncOrderRiskFlat();
            log.info("Order OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Order OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncOrderDetailsFlat() {
        log.info("Syncing order_details_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrderSync.truncateOrderDetailsFlat");
            int rows = session.insert("OrderSync.syncOrderDetailsFlat");
            session.commit();
            log.info("order_details_flat: {} rows synced", rows);
        }
    }

    public void syncOrderItemsFlat() {
        log.info("Syncing order_items_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrderSync.truncateOrderItemsFlat");
            int rows = session.insert("OrderSync.syncOrderItemsFlat");
            session.commit();
            log.info("order_items_flat: {} rows synced", rows);
        }
    }

    public void syncOrderActivityFlat() {
        log.info("Syncing order_activity_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrderSync.truncateOrderActivityFlat");
            int rows = session.insert("OrderSync.syncOrderActivityFlat");
            session.commit();
            log.info("order_activity_flat: {} rows synced", rows);
        }
    }

    public void syncShipmentTrackingFlat() {
        log.info("Syncing shipment_tracking_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrderSync.truncateShipmentTrackingFlat");
            int rows = session.insert("OrderSync.syncShipmentTrackingFlat");
            session.commit();
            log.info("shipment_tracking_flat: {} rows synced", rows);
        }
    }

    public void syncOrderRiskFlat() {
        log.info("Syncing order_risk_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OrderSync.truncateOrderRiskFlat");
            int rows = session.insert("OrderSync.syncOrderRiskFlat");
            session.commit();
            log.info("order_risk_flat: {} rows synced", rows);
        }
    }
}
