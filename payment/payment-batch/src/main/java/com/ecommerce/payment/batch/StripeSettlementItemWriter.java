package com.ecommerce.payment.batch;

import com.ecommerce.payment.domain.ReconciliationMismatch;
import com.ecommerce.payment.repository.ReconciliationMismatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class StripeSettlementItemWriter implements ItemWriter<ReconciliationMismatch> {

    private static final Logger log = LoggerFactory.getLogger(StripeSettlementItemWriter.class);

    private final ReconciliationMismatchRepository mismatchRepository;

    public StripeSettlementItemWriter(ReconciliationMismatchRepository mismatchRepository) {
        this.mismatchRepository = mismatchRepository;
    }

    @Override
    public void write(Chunk<? extends ReconciliationMismatch> chunk) throws Exception {
        log.info("Writing {} reconciliation mismatches to database...", chunk.getItems().size());

        for (ReconciliationMismatch mismatch : chunk.getItems()) {
            // Check if this specific mismatch already exists to avoid duplicates
            boolean exists = mismatchRepository
                    .findByGatewayTypeAndProviderTransactionIdAndResolvedFalse(
                            mismatch.getGatewayType(),
                            mismatch.getProviderTransactionId())
                    .isPresent();

            if (!exists) {
                mismatchRepository.save(mismatch);
                log.warn("Saved new reconciliation mismatch for providerTransactionId {}", mismatch.getProviderTransactionId());
            } else {
                log.debug("Unresolved mismatch already exists for providerTransactionId {}", mismatch.getProviderTransactionId());
            }
        }
    }
}
