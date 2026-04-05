package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates warehouse OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.dim_warehouse             (warehouses + inventory stats + fulfillment stats)
 *   - hm_query.warehouse_locations_flat   (warehouse_locations + warehouses)
 *   - hm_query.warehouse_inventory_flat   (warehouse_inventory + locations + products)
 *
 * Runs nightly at 3:40 AM IST.
 */
public class WarehouseSyncService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public WarehouseSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 40 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting warehouse OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimWarehouse();
            syncWarehouseLocationsFlat();
            syncWarehouseInventoryFlat();
            log.info("Warehouse OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Warehouse OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimWarehouse() {
        log.info("Syncing dim_warehouse...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WarehouseSync.truncateDimWarehouse");
            int rows = session.insert("WarehouseSync.syncDimWarehouse");
            session.commit();
            log.info("dim_warehouse: {} rows synced", rows);
        }
    }

    public void syncWarehouseLocationsFlat() {
        log.info("Syncing warehouse_locations_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WarehouseSync.truncateWarehouseLocationsFlat");
            int rows = session.insert("WarehouseSync.syncWarehouseLocationsFlat");
            session.commit();
            log.info("warehouse_locations_flat: {} rows synced", rows);
        }
    }

    public void syncWarehouseInventoryFlat() {
        log.info("Syncing warehouse_inventory_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("WarehouseSync.truncateWarehouseInventoryFlat");
            int rows = session.insert("WarehouseSync.syncWarehouseInventoryFlat");
            session.commit();
            log.info("warehouse_inventory_flat: {} rows synced", rows);
        }
    }
}
