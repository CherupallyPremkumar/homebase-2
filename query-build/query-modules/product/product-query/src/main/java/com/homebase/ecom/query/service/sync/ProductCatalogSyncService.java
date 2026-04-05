package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query OLAP tables from OLTP source tables.
 *
 * Strategy: TRUNCATE + INSERT (full refresh).
 * Runs nightly at 2:00 AM IST via @Scheduled.
 * Can also be triggered manually via /admin/sync/products endpoint.
 *
 * Tables synced:
 *   - hm_query.product_catalog_flat   (from products + variants + inventory + supplier + reviews)
 *   - hm_query.product_variant_flat   (from product_variants + inventory_item)
 *   - hm_query.category_config_flat   (from categories + tax_mapping + attributes + products)
 *
 * MyBatis mapper: product-sync.xml (namespace: ProductSync)
 */
public class ProductCatalogSyncService {

    private static final Logger log = LoggerFactory.getLogger(ProductCatalogSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ProductCatalogSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Full sync — runs nightly at 2:00 AM IST.
     * Truncates all 3 OLAP tables and repopulates from OLTP.
     */
    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting product catalog OLAP sync...");
        long start = System.currentTimeMillis();

        try {
            syncProductCatalogFlat();
            syncProductVariantFlat();
            syncCategoryConfigFlat();
            syncBrandRegistryFlat();
            syncAttributeDefinitionFlat();
            syncAttributeSetFlat();

            long duration = System.currentTimeMillis() - start;
            log.info("Product catalog OLAP sync completed in {}ms", duration);
        } catch (Exception e) {
            log.error("Product catalog OLAP sync FAILED", e);
            throw e;
        }
    }

    /**
     * Sync product_catalog_flat.
     * Source: products + categories + inventory_item + supplier + product_reviews + order_items
     */
    public void syncProductCatalogFlat() {
        log.info("Syncing product_catalog_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateProductCatalogFlat");
            int rows = session.insert("ProductSync.syncProductCatalogFlat");
            session.commit();
            log.info("product_catalog_flat: {} rows synced", rows);
        }
    }

    /**
     * Sync product_variant_flat.
     * Source: product_variants + variant_attributes + inventory_item
     */
    public void syncProductVariantFlat() {
        log.info("Syncing product_variant_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateProductVariantFlat");
            int rows = session.insert("ProductSync.syncProductVariantFlat");
            session.commit();
            log.info("product_variant_flat: {} rows synced", rows);
        }
    }

    /**
     * Sync category_config_flat.
     * Source: categories + tax_category_mapping + tax_rates + category_attribute_mapping + products
     */
    public void syncCategoryConfigFlat() {
        log.info("Syncing category_config_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateCategoryConfigFlat");
            int rows = session.insert("ProductSync.syncCategoryConfigFlat");
            session.commit();
            log.info("category_config_flat: {} rows synced", rows);
        }
    }

    /**
     * Sync brand_registry_flat.
     * Source: brands + authorized_sellers + brand_trademarks + ip_violations + brand_analytics + products
     */
    public void syncBrandRegistryFlat() {
        log.info("Syncing brand_registry_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateBrandRegistryFlat");
            int rows = session.insert("ProductSync.syncBrandRegistryFlat");
            session.commit();
            log.info("brand_registry_flat: {} rows synced", rows);
        }
    }

    /**
     * Sync attribute_definition_flat.
     * Source: attribute_definitions + variant_attributes + product_attribute_values + category_attribute_mapping + attribute_options
     */
    public void syncAttributeDefinitionFlat() {
        log.info("Syncing attribute_definition_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateAttributeDefinitionFlat");
            int rows = session.insert("ProductSync.syncAttributeDefinitionFlat");
            session.commit();
            log.info("attribute_definition_flat: {} rows synced", rows);
        }
    }

    /**
     * Sync attribute_set_flat.
     * Source: attribute_sets + attribute_set_groups + attribute_set_attributes + attribute_definitions
     */
    public void syncAttributeSetFlat() {
        log.info("Syncing attribute_set_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ProductSync.truncateAttributeSetFlat");
            int rows = session.insert("ProductSync.syncAttributeSetFlat");
            session.commit();
            log.info("attribute_set_flat: {} rows synced", rows);
        }
    }
}
