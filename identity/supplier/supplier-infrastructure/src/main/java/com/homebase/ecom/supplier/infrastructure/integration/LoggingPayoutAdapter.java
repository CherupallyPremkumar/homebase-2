package com.homebase.ecom.supplier.infrastructure.integration;

import com.homebase.ecom.supplier.domain.port.PayoutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for supplier payout operations.
 *
 * Currently a logging stub — will delegate to settlement-client
 * once cross-service event wiring is complete.
 */
public class LoggingPayoutAdapter implements PayoutPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingPayoutAdapter.class);

    @Override
    public void holdPayouts(String supplierId) {
        log.info("Holding all pending payouts for supplierId={}", supplierId);
    }

    @Override
    public void releasePayouts(String supplierId) {
        log.info("Releasing held payouts for supplierId={}", supplierId);
    }
}
