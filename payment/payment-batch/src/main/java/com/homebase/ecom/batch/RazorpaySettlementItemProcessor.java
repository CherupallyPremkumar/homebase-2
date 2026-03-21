package com.homebase.ecom.batch;

import com.homebase.ecom.batch.dto.RazorpaySettlementRow;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentTransaction;
import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationMismatch;
import com.homebase.ecom.payment.infrastructure.persistence.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RazorpaySettlementItemProcessor implements ItemProcessor<RazorpaySettlementRow, ReconciliationMismatch> {

    private static final Logger log = LoggerFactory.getLogger(RazorpaySettlementItemProcessor.class);

    private final PaymentTransactionRepository paymentTransactionRepository;

    public RazorpaySettlementItemProcessor(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public ReconciliationMismatch process(RazorpaySettlementRow row) throws Exception {
        log.debug("Processing Razorpay payment: {}", row.getPaymentId());

        PaymentTransaction dbTx = paymentTransactionRepository.findByGatewayChargeId(row.getPaymentId()).orElse(null);

        // Scenario 1: Missing in DB
        if (dbTx == null) {
            ReconciliationMismatch mismatch = new ReconciliationMismatch();
            mismatch.setGatewayType("razorpay");
            mismatch.setProviderTransactionId(row.getPaymentId());
            mismatch.setMismatchType("MISSING_IN_DB");
            mismatch.setProviderAmount(row.getAmount());
            mismatch.setProviderStatus(row.getStatus());
            return mismatch;
        }

        boolean hasMismatch = false;
        ReconciliationMismatch mismatch = new ReconciliationMismatch();
        mismatch.setGatewayType("razorpay");
        mismatch.setProviderTransactionId(row.getPaymentId());
        mismatch.setOrderId(dbTx.getOrderId());
        mismatch.setInternalAmount(dbTx.getAmount() != null ? dbTx.getAmount().toMajorUnits() : null);
        mismatch.setProviderAmount(row.getAmount());

        // Scenario 2: Amount Mismatch
        if (dbTx.getAmount() == null
                || dbTx.getAmount().toMajorUnits().compareTo(row.getAmount()) != 0) {
            mismatch.setMismatchType("AMOUNT_MISMATCH");
            hasMismatch = true;
        }

        // Scenario 3: Status Mismatch
        String mappedStatus = row.getStatus() != null ? row.getStatus().toUpperCase() : "UNKNOWN";
        if (!dbTx.getStatus().equalsIgnoreCase(mappedStatus)) {
            if (hasMismatch) {
                mismatch.setMismatchType("AMOUNT_AND_STATUS_MISMATCH");
            } else {
                mismatch.setMismatchType("STATUS_MISMATCH");
                hasMismatch = true;
            }
            mismatch.setProviderStatus(mappedStatus);
            mismatch.setInternalStatus(dbTx.getStatus());
        }

        if (hasMismatch) {
            log.warn("Mismatch found for Razorpay payment: {}", row.getPaymentId());
            return mismatch;
        }

        return null;
    }
}
