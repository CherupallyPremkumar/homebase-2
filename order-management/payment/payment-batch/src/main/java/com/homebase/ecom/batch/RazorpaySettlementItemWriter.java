package com.homebase.ecom.batch;

import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationMismatch;
import com.homebase.ecom.payment.infrastructure.persistence.repository.ReconciliationMismatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class RazorpaySettlementItemWriter implements ItemWriter<ReconciliationMismatch> {

    private static final Logger log = LoggerFactory.getLogger(RazorpaySettlementItemWriter.class);

    private final ReconciliationMismatchRepository mismatchRepository;

    public RazorpaySettlementItemWriter(ReconciliationMismatchRepository mismatchRepository) {
        this.mismatchRepository = mismatchRepository;
    }

    @Override
    public void write(Chunk<? extends ReconciliationMismatch> chunk) throws Exception {
        log.info("Writing {} Razorpay reconciliation mismatches to database...", chunk.getItems().size());

        for (ReconciliationMismatch mismatch : chunk.getItems()) {
            boolean exists = mismatchRepository
                    .findByGatewayTypeAndProviderTransactionIdAndResolvedFalse(
                            mismatch.getGatewayType(),
                            mismatch.getProviderTransactionId())
                    .isPresent();

            if (!exists) {
                mismatchRepository.save(mismatch);
                log.warn("Saved new Razorpay reconciliation mismatch for paymentId {}",
                        mismatch.getProviderTransactionId());
            }
        }
    }
}
