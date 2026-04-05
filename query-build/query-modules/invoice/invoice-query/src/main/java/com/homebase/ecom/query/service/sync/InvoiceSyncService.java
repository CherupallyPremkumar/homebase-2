package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates invoice OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.invoice_flat  (invoices + orders + user_profiles + order_addresses)
 *
 * Runs nightly at 3:20 AM IST.
 */
public class InvoiceSyncService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public InvoiceSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 20 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting invoice OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncInvoiceFlat();
            log.info("Invoice OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Invoice OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncInvoiceFlat() {
        log.info("Syncing invoice_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("InvoiceSync.truncateInvoiceFlat");
            int rows = session.insert("InvoiceSync.syncInvoiceFlat");
            session.commit();
            log.info("invoice_flat: {} rows synced", rows);
        }
    }
}
