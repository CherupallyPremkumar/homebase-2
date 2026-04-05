package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates promo/campaign OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.coupon_flat     (promo codes / coupons)
 *   - hm_query.campaign_flat   (marketing campaigns with budget/ROI)
 *
 * Runs nightly at 2:10 AM IST.
 */
public class PromoSyncService {

    private static final Logger log = LoggerFactory.getLogger(PromoSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public PromoSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 10 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting promo OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncCouponFlat();
            syncCampaignFlat();
            log.info("Promo OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Promo OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncCouponFlat() {
        log.info("Syncing coupon_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PromoSync.truncateCouponFlat");
            int rows = session.insert("PromoSync.syncCouponFlat");
            session.commit();
            log.info("coupon_flat: {} rows synced", rows);
        }
    }

    public void syncCampaignFlat() {
        log.info("Syncing campaign_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PromoSync.truncateCampaignFlat");
            int rows = session.insert("PromoSync.syncCampaignFlat");
            session.commit();
            log.info("campaign_flat: {} rows synced", rows);
        }
    }
}
