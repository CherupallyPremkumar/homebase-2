package com.ecommerce.payment.batch;

import com.ecommerce.payment.batch.dto.StripeSettlementRow;
import com.ecommerce.payment.domain.PaymentTransaction;
import com.ecommerce.payment.domain.ReconciliationMismatch;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StripeSettlementItemProcessor implements ItemProcessor<StripeSettlementRow, ReconciliationMismatch> {

    private static final Logger log = LoggerFactory.getLogger(StripeSettlementItemProcessor.class);

    private final PaymentTransactionRepository paymentTransactionRepository;

    public StripeSettlementItemProcessor(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public ReconciliationMismatch process(StripeSettlementRow row) throws Exception {
        log.debug("Processing Stripe charge: {}", row.getChargeId());

        PaymentTransaction dbTx = paymentTransactionRepository.findByGatewayChargeId(row.getChargeId()).orElse(null);

        // Scenario 1: Missing in DB
        if (dbTx == null) {
            ReconciliationMismatch mismatch = new ReconciliationMismatch();
            mismatch.setGatewayType("stripe");
            mismatch.setProviderTransactionId(row.getChargeId());
            mismatch.setMismatchType("MISSING_IN_DB");
            mismatch.setProviderAmount(row.getAmount());
            mismatch.setProviderStatus(row.getStatus());
            return mismatch;
        }

        boolean hasMismatch = false;
        ReconciliationMismatch mismatch = new ReconciliationMismatch();
        mismatch.setGatewayType("stripe");
        mismatch.setProviderTransactionId(row.getChargeId());
        mismatch.setOrderId(dbTx.getOrderId());
        mismatch.setInternalAmount(dbTx.getAmount());
        mismatch.setProviderAmount(row.getAmount());

        // Scenario 2: Amount Mismatch
        if (dbTx.getAmount() == null || dbTx.getAmount().compareTo(row.getAmount()) != 0) {
            mismatch.setMismatchType("AMOUNT_MISMATCH");
            hasMismatch = true;
        }

        // Scenario 3: Status Mismatch (Stripe uses 'succeeded', 'failed', application
        // uses 'SUCCEEDED')
        String mappedStripeStatus = row.getStatus() != null ? row.getStatus().toUpperCase() : "UNKNOWN";
        if (!dbTx.getStatus().equalsIgnoreCase(mappedStripeStatus)) {
            // Only overwrite mismatchType if it wasn't already set, or append it
            if (hasMismatch) {
                mismatch.setMismatchType("AMOUNT_AND_STATUS_MISMATCH");
            } else {
                mismatch.setMismatchType("STATUS_MISMATCH");
                hasMismatch = true;
            }
            mismatch.setProviderStatus(mappedStripeStatus);
            mismatch.setInternalStatus(dbTx.getStatus());
        }

        if (hasMismatch) {
            log.warn("Mismatch found for charge: {}", row.getChargeId());
            return mismatch;
        }

        // Return null to indicate no mismatch (filtered out by writer)
        return null;
    }
}
