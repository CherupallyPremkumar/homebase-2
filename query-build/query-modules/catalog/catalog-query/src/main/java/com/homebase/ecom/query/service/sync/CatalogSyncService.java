package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Catalog Query module.
 *
 * Populates hm_query OLAP tables from OLTP catalog source schemas.
 * The core dim_product and dim_category tables are synced by UiQuerySyncService
 * (shared dimensions). This service syncs catalog-specific tables only.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:20 AM IST via @Scheduled.
 *
 * Tables synced:
 *   - hm_query.collection_flat   (from collections)
 *   - hm_query.product_tag_flat  (from catalog_item_tags + catalog_items)
 *
 * Note: dim_product, dim_category, category_config_flat, product_catalog_flat
 *       are synced by ProductCatalogSyncService and UiQuerySyncService.
 *
 * MyBatis mapper: catalog-sync.xml (namespace: CatalogSync)
 */
public class CatalogSyncService {

    private static final Logger log = LoggerFactory.getLogger(CatalogSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public CatalogSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync — runs nightly at 2:20 AM IST.
     */
    @Scheduled(cron = "0 20 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting Catalog OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncCollectionFlat();
            syncProductTagFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("Catalog OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("Catalog OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCollectionFlat() {
        log.info("Syncing collection_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CatalogSync.truncateCollectionFlat");
            int rows = session.insert("CatalogSync.syncCollectionFlat");
            session.commit();
            log.info("collection_flat: {} rows synced", rows);
        }
    }

    public void syncProductTagFlat() {
        log.info("Syncing product_tag_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("CatalogSync.truncateProductTagFlat");
            int rows = session.insert("CatalogSync.syncProductTagFlat");
            session.commit();
            log.info("product_tag_flat: {} rows synced", rows);
        }
    }
}
