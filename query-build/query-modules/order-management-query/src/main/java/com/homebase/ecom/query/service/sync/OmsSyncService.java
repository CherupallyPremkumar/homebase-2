package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Order Management (OMS) Query module.
 *
 * Populates hm_query OLAP tables from OLTP source schemas for OMS dashboard.
 * The core order_details_flat and order_items_flat tables are synced by
 * OrderSyncService. This service syncs OMS-specific supplementary tables.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:30 AM IST via @Scheduled.
 *
 * Tables synced:
 *   - hm_query.fulfillment_flat      (from fulfillment_orders + warehouses)
 *   - hm_query.settlement_flat       (from settlements + supplier)
 *   - hm_query.order_activity_flat   (from order_activity)
 *
 * MyBatis mapper: oms-sync.xml (namespace: OmsSync)
 */
public class OmsSyncService {

    private static final Logger log = LoggerFactory.getLogger(OmsSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public OmsSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync — runs nightly at 2:30 AM IST.
     */
    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting OMS OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncFulfillmentFlat();
            syncSettlementFlat();
            syncOrderActivityFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("OMS OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("OMS OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncFulfillmentFlat() {
        log.info("Syncing fulfillment_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OmsSync.truncateFulfillmentFlat");
            int rows = session.insert("OmsSync.syncFulfillmentFlat");
            session.commit();
            log.info("fulfillment_flat: {} rows synced", rows);
        }
    }

    public void syncSettlementFlat() {
        log.info("Syncing settlement_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OmsSync.truncateSettlementFlat");
            int rows = session.insert("OmsSync.syncSettlementFlat");
            session.commit();
            log.info("settlement_flat: {} rows synced", rows);
        }
    }

    public void syncOrderActivityFlat() {
        log.info("Syncing order_activity_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OmsSync.truncateOrderActivityFlat");
            int rows = session.insert("OmsSync.syncOrderActivityFlat");
            session.commit();
            log.info("order_activity_flat: {} rows synced", rows);
        }
    }
}
