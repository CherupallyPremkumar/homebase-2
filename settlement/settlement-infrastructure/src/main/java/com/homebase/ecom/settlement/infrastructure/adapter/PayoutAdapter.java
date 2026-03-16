package com.homebase.ecom.settlement.infrastructure.adapter;

import com.homebase.ecom.settlement.domain.port.PayoutPort;
import com.homebase.ecom.settlement.model.Settlement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Default adapter for payout operations.
 * In production, this would connect to a real payment gateway or banking API.
 */
public class PayoutAdapter implements PayoutPort {

    private static final Logger log = LoggerFactory.getLogger(PayoutAdapter.class);

    @Override
    public String initiatePayout(Settlement settlement) {
        String reference = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Payout initiated for settlement {} to supplier {}, netAmount={}, reference={}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetAmount(), reference);
        return reference;
    }

    @Override
    public boolean isPayoutSuccessful(String disbursementReference) {
        log.info("Checking payout status for reference: {}", disbursementReference);
        return true; // Default: assume success
    }
}
