package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query pricing OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.pricing_rule_flat     (from pricing_rules + orders + products)
 *   - hm_query.price_change_log_flat (from price_history + products, last 90 days)
 *
 * Runs nightly at 2:30 AM IST.
 */
public class PricingSyncService {

    private static final Logger log = LoggerFactory.getLogger(PricingSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public PricingSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting pricing OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncPricingRuleFlat();
            syncPriceChangeLogFlat();
            log.info("Pricing OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Pricing OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncPricingRuleFlat() {
        log.info("Syncing pricing_rule_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PricingSync.truncatePricingRuleFlat");
            int rows = session.insert("PricingSync.syncPricingRuleFlat");
            session.update("PricingSync.detectRuleConflicts");
            session.commit();
            log.info("pricing_rule_flat: {} rows synced + conflicts detected", rows);
        }
    }

    public void syncPriceChangeLogFlat() {
        log.info("Syncing price_change_log_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PricingSync.truncatePriceChangeLogFlat");
            int rows = session.insert("PricingSync.syncPriceChangeLogFlat");
            session.commit();
            log.info("price_change_log_flat: {} rows synced", rows);
        }
    }
}
