package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates shipping OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.fact_shipments   (shipments + orders + user_profiles)
 *   - hm_query.dim_carrier      (distinct carriers — upsert)
 *   - hm_query.dim_geography    (order shipping addresses — upsert)
 *
 * Read queries JOIN fact_shipments with dim_carrier and dim_geography
 * for star-schema reads.
 *
 * Runs nightly at 3:30 AM IST.
 */
public class ShippingSyncService {

    private static final Logger log = LoggerFactory.getLogger(ShippingSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public ShippingSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 30 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting shipping OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimCarrier();
            syncDimGeography();
            syncFactShipments();
            log.info("Shipping OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Shipping OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimCarrier() {
        log.info("Syncing dim_carrier...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("ShippingSync.syncDimCarrier");
            session.commit();
            log.info("dim_carrier: {} rows upserted", rows);
        }
    }

    public void syncDimGeography() {
        log.info("Syncing dim_geography...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("ShippingSync.syncDimGeography");
            session.commit();
            log.info("dim_geography: {} rows upserted", rows);
        }
    }

    public void syncFactShipments() {
        log.info("Syncing fact_shipments...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("ShippingSync.truncateFactShipments");
            int rows = session.insert("ShippingSync.syncFactShipments");
            session.commit();
            log.info("fact_shipments: {} rows synced", rows);
        }
    }
}
