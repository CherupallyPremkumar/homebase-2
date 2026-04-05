package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates WMS (Warehouse Management System) OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.dim_warehouse                     (warehouses + inventory stats + fulfillment stats — upsert)
 *   - hm_query.wms_overview_stats_flat            (precomputed KPIs per warehouse + __ALL__ rollup)
 *   - hm_query.wms_inventory_with_product_flat    (inventory_item + products + warehouses)
 *   - hm_query.wms_fulfillment_with_order_flat    (fulfillment_orders + orders + warehouses + shipments)
 *   - hm_query.wms_pick_list_flat                 (pick_lists + fulfillment + warehouses + orders)
 *   - hm_query.wms_fulfillment_line_items_flat    (fulfillment_line_items + products + order_items)
 *   - hm_query.wms_inventory_by_location_flat     (warehouse_inventory + locations + products)
 *   - hm_query.wms_inbound_shipments_flat         (inventory_movements + inventory_item + supplier + products)
 *
 * Cross-BC reads: warehouse, inventory, fulfillment, product, order, user, shipping, supplier.
 *
 * Runs nightly at 3:45 AM IST.
 */
public class WmsSyncService {

    private static final Logger log = LoggerFactory.getLogger(WmsSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public WmsSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 45 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting WMS OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimWarehouse();
            syncWmsOverviewStatsFlat();
            syncWmsInventoryWithProductFlat();
            syncWmsFulfillmentWithOrderFlat();
            syncWmsPickListFlat();
            syncWmsFulfillmentLineItemsFlat();
            syncWmsInventoryByLocationFlat();
            syncWmsInboundShipmentsFlat();
            log.info("WMS OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("WMS OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimWarehouse() {
        log.info("Syncing dim_warehouse for WMS...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("WmsSync.syncDimWarehouse");
            session.commit();
            log.info("dim_warehouse (WMS): {} rows upserted", rows);
        }
    }

    public void syncWmsOverviewStatsFlat() {
        log.info("Syncing wms_overview_stats_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsOverviewStatsFlat");
            int perWarehouse = session.insert("WmsSync.syncWmsOverviewStatsFlat");
            int allRollup = session.insert("WmsSync.syncWmsOverviewStatsAllWarehouse");
            session.commit();
            log.info("wms_overview_stats_flat: {} per-warehouse + {} rollup rows synced", perWarehouse, allRollup);
        }
    }

    public void syncWmsInventoryWithProductFlat() {
        log.info("Syncing wms_inventory_with_product_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsInventoryWithProductFlat");
            int rows = session.insert("WmsSync.syncWmsInventoryWithProductFlat");
            session.commit();
            log.info("wms_inventory_with_product_flat: {} rows synced", rows);
        }
    }

    public void syncWmsFulfillmentWithOrderFlat() {
        log.info("Syncing wms_fulfillment_with_order_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsFulfillmentWithOrderFlat");
            int rows = session.insert("WmsSync.syncWmsFulfillmentWithOrderFlat");
            session.commit();
            log.info("wms_fulfillment_with_order_flat: {} rows synced", rows);
        }
    }

    public void syncWmsPickListFlat() {
        log.info("Syncing wms_pick_list_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsPickListFlat");
            int rows = session.insert("WmsSync.syncWmsPickListFlat");
            session.commit();
            log.info("wms_pick_list_flat: {} rows synced", rows);
        }
    }

    public void syncWmsFulfillmentLineItemsFlat() {
        log.info("Syncing wms_fulfillment_line_items_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsFulfillmentLineItemsFlat");
            int rows = session.insert("WmsSync.syncWmsFulfillmentLineItemsFlat");
            session.commit();
            log.info("wms_fulfillment_line_items_flat: {} rows synced", rows);
        }
    }

    public void syncWmsInventoryByLocationFlat() {
        log.info("Syncing wms_inventory_by_location_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsInventoryByLocationFlat");
            int rows = session.insert("WmsSync.syncWmsInventoryByLocationFlat");
            session.commit();
            log.info("wms_inventory_by_location_flat: {} rows synced", rows);
        }
    }

    public void syncWmsInboundShipmentsFlat() {
        log.info("Syncing wms_inbound_shipments_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WmsSync.truncateWmsInboundShipmentsFlat");
            int rows = session.insert("WmsSync.syncWmsInboundShipmentsFlat");
            session.commit();
            log.info("wms_inbound_shipments_flat: {} rows synced", rows);
        }
    }
}
