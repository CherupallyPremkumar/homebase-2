package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates fulfillment OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.fulfillment_flat            (fulfillment_orders + warehouses)
 *   - hm_query.fulfillment_line_items_flat (fulfillment_line_items + products + order_items)
 *
 * Runs nightly at 3:35 AM IST.
 */
public class FulfillmentSyncService {

    private static final Logger log = LoggerFactory.getLogger(FulfillmentSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public FulfillmentSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 35 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting fulfillment OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncFulfillmentFlat();
            syncFulfillmentLineItemsFlat();
            log.info("Fulfillment OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Fulfillment OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncFulfillmentFlat() {
        log.info("Syncing fulfillment_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("FulfillmentSync.truncateFulfillmentFlat");
            int rows = session.insert("FulfillmentSync.syncFulfillmentFlat");
            session.commit();
            log.info("fulfillment_flat: {} rows synced", rows);
        }
    }

    public void syncFulfillmentLineItemsFlat() {
        log.info("Syncing fulfillment_line_items_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("FulfillmentSync.truncateFulfillmentLineItemsFlat");
            int rows = session.insert("FulfillmentSync.syncFulfillmentLineItemsFlat");
            session.commit();
            log.info("fulfillment_line_items_flat: {} rows synced", rows);
        }
    }
}
