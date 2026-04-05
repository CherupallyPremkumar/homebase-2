package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates tax OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.tax_rates_flat
 *   - hm_query.tax_category_mappings_flat
 *   - hm_query.tax_regions_flat
 *   - hm_query.tax_exemptions_flat
 *   - hm_query.order_tax_lines_flat
 *
 * Runs nightly at 3:40 AM IST.
 */
public class TaxSyncService {

    private static final Logger log = LoggerFactory.getLogger(TaxSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public TaxSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 40 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting tax OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncTaxRatesFlat();
            syncTaxCategoryMappingsFlat();
            syncTaxRegionsFlat();
            syncTaxExemptionsFlat();
            syncOrderTaxLinesFlat();
            log.info("Tax OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Tax OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncTaxRatesFlat() {
        log.info("Syncing tax_rates_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("TaxSync.truncateTaxRatesFlat");
            int rows = session.insert("TaxSync.syncTaxRatesFlat");
            session.commit();
            log.info("tax_rates_flat: {} rows synced", rows);
        }
    }

    public void syncTaxCategoryMappingsFlat() {
        log.info("Syncing tax_category_mappings_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("TaxSync.truncateTaxCategoryMappingsFlat");
            int rows = session.insert("TaxSync.syncTaxCategoryMappingsFlat");
            session.commit();
            log.info("tax_category_mappings_flat: {} rows synced", rows);
        }
    }

    public void syncTaxRegionsFlat() {
        log.info("Syncing tax_regions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("TaxSync.truncateTaxRegionsFlat");
            int rows = session.insert("TaxSync.syncTaxRegionsFlat");
            session.commit();
            log.info("tax_regions_flat: {} rows synced", rows);
        }
    }

    public void syncTaxExemptionsFlat() {
        log.info("Syncing tax_exemptions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("TaxSync.truncateTaxExemptionsFlat");
            int rows = session.insert("TaxSync.syncTaxExemptionsFlat");
            session.commit();
            log.info("tax_exemptions_flat: {} rows synced", rows);
        }
    }

    public void syncOrderTaxLinesFlat() {
        log.info("Syncing order_tax_lines_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("TaxSync.truncateOrderTaxLinesFlat");
            int rows = session.insert("TaxSync.syncOrderTaxLinesFlat");
            session.commit();
            log.info("order_tax_lines_flat: {} rows synced", rows);
        }
    }
}
