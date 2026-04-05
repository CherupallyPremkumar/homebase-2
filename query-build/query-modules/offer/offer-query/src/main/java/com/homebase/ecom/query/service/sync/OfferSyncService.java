package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates hm_query.offer_flat from OLTP.
 *
 * Source: hm_offer.offer JOIN hm_product.products JOIN hm_supplier.supplier
 * Target: hm_query.offer_flat
 *
 * Runs nightly at 4:00 AM IST.
 */
public class OfferSyncService {

    private static final Logger log = LoggerFactory.getLogger(OfferSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public OfferSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting offer OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncOfferFlat();
            log.info("Offer OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Offer OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncOfferFlat() {
        log.info("Syncing offer_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("OfferSync.truncateOfferFlat");
            int rows = session.insert("OfferSync.syncOfferFlat");
            session.commit();
            log.info("offer_flat: {} rows synced", rows);
        }
    }
}
