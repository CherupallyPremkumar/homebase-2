package com.ecommerce.payment.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NightAuditBatchJob {

    private static final Logger log = LoggerFactory.getLogger(NightAuditBatchJob.class);

    private final GatewayReconciliationDispatcher reconciliationDispatcher;

    public NightAuditBatchJob(GatewayReconciliationDispatcher reconciliationDispatcher) {
        this.reconciliationDispatcher = reconciliationDispatcher;
    }

    // Run every night at 11:59 PM (23:59:00)
    @Scheduled(cron = "0 59 23 * * ?")
    public void runNightlyAudit() {
        String activeGateway = reconciliationDispatcher.getActiveGatewayType();
        var gateways = reconciliationDispatcher.getSupportedGatewayTypes();

        log.info("Starting scheduled Nightly Audit Job for gateway reconciliation (activeGateway={}, gateways={})...",
                activeGateway, gateways);

        for (String gateway : gateways) {
            log.info("Running nightly reconciliation for gateway={}", gateway);
            reconciliationDispatcher.triggerReconciliationJob(gateway);
        }

        log.info("Nightly Audit Job finished executing.");
    }
}
