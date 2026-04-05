package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service for Supplier Dashboard Query module.
 *
 * The supplier dashboard reads from shared hm_query.* flat tables that are
 * synced by other services:
 *   - seller_dashboard_flat      (SellerDashboardSyncService)
 *   - order_details_flat         (OrderSyncService)
 *   - order_items_flat           (OrderSyncService)
 *   - settlement_flat            (OmsSyncService)
 *   - product_catalog_flat       (PlatformDashboardSyncService)
 *   - inventory_health_flat      (PlatformDashboardSyncService)
 *   - return_detail_flat         (UiQuerySyncService)
 *   - fulfillment_flat           (OmsSyncService)
 *
 * No supplier-specific flat tables are needed at this time.
 * This service exists as a placeholder for future supplier-specific
 * OLAP tables (e.g. supplier_compliance_flat, supplier_onboarding_flat).
 *
 * MyBatis mapper: supplier-dashboard-sync.xml (namespace: SupplierDashboardSync)
 */
public class SupplierDashboardSyncService {

    private static final Logger log = LoggerFactory.getLogger(SupplierDashboardSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public SupplierDashboardSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Placeholder sync -- runs nightly at 2:40 AM IST.
     * Currently a no-op since all underlying tables are synced by other services.
     * Add supplier-specific flat table syncs here as needed.
     */
    @Scheduled(cron = "0 40 2 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Supplier Dashboard OLAP sync: all underlying tables synced by other services. No additional sync needed.");
    }
}
