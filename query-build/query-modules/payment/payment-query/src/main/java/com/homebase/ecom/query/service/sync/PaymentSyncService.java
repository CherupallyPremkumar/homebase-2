package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates payment OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.fact_payments            (payment)
 *   - hm_query.dim_customer             (user_profiles — upsert)
 *   - hm_query.dim_payment_method       (distinct payment methods — upsert)
 *   - hm_query.payment_refunds_flat     (payment_refunds)
 *   - hm_query.payment_webhook_log_flat (payment_webhook_log — last 90 days)
 *
 * Read queries JOIN fact_payments with dim_customer and dim_payment_method
 * for star-schema reads.
 *
 * Runs nightly at 3:15 AM IST.
 */
public class PaymentSyncService {

    private static final Logger log = LoggerFactory.getLogger(PaymentSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public PaymentSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 15 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting payment OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncDimCustomer();
            syncDimPaymentMethod();
            syncFactPayments();
            syncPaymentRefundsFlat();
            syncPaymentWebhookLogFlat();
            log.info("Payment OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Payment OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncDimCustomer() {
        log.info("Syncing dim_customer for payments...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("PaymentSync.syncDimCustomer");
            session.commit();
            log.info("dim_customer (payments): {} rows upserted", rows);
        }
    }

    public void syncDimPaymentMethod() {
        log.info("Syncing dim_payment_method...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int rows = session.update("PaymentSync.syncDimPaymentMethod");
            session.commit();
            log.info("dim_payment_method: {} rows upserted", rows);
        }
    }

    public void syncFactPayments() {
        log.info("Syncing fact_payments...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PaymentSync.truncateFactPayments");
            int rows = session.insert("PaymentSync.syncFactPayments");
            session.commit();
            log.info("fact_payments: {} rows synced", rows);
        }
    }

    public void syncPaymentRefundsFlat() {
        log.info("Syncing payment_refunds_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PaymentSync.truncatePaymentRefundsFlat");
            int rows = session.insert("PaymentSync.syncPaymentRefundsFlat");
            session.commit();
            log.info("payment_refunds_flat: {} rows synced", rows);
        }
    }

    public void syncPaymentWebhookLogFlat() {
        log.info("Syncing payment_webhook_log_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("PaymentSync.truncatePaymentWebhookLogFlat");
            int rows = session.insert("PaymentSync.syncPaymentWebhookLogFlat");
            session.commit();
            log.info("payment_webhook_log_flat: {} rows synced", rows);
        }
    }
}
