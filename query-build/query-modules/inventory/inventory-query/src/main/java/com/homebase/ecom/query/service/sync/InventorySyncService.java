package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query inventory OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.inventory_health_flat  (from inventory_item + products + supplier + warehouses)
 *   - hm_query.stock_movement_flat    (from inventory_movements, last 30 days)
 *
 * Runs nightly at 2:15 AM IST (offset from product sync at 2:00 AM).
 */
public class InventorySyncService {

    private static final Logger log = LoggerFactory.getLogger(InventorySyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public InventorySyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 15 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting inventory OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncInventoryHealthFlat();
            syncStockMovementFlat();
            log.info("Inventory OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Inventory OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncInventoryHealthFlat() {
        log.info("Syncing inventory_health_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("InventorySync.truncateInventoryHealthFlat");
            int rows = session.insert("InventorySync.syncInventoryHealthFlat");
            session.commit();
            log.info("inventory_health_flat: {} rows synced", rows);
        }
    }

    public void syncStockMovementFlat() {
        log.info("Syncing stock_movement_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("InventorySync.truncateStockMovementFlat");
            int rows = session.insert("InventorySync.syncStockMovementFlat");
            session.commit();
            log.info("stock_movement_flat: {} rows synced", rows);
        }
    }
}
