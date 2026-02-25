package com.ecommerce.payment.batch;

import com.ecommerce.payment.domain.ReconciliationCategory;
import com.ecommerce.payment.domain.ReconciliationItem;
import com.ecommerce.payment.domain.ReconciliationItemStatus;
import com.ecommerce.payment.domain.ReconciliationRun;
import com.ecommerce.payment.domain.ReconciliationRunStatus;
import com.ecommerce.payment.domain.ReconciliationSeverity;
import com.ecommerce.payment.repository.ReconciliationItemRepository;
import com.ecommerce.payment.repository.ReconciliationMismatchRepository;
import com.ecommerce.payment.repository.ReconciliationRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Bridge/migration helper:
 *
 * Current reconciliation job writes to legacy reconciliation_mismatches (Stripe-specific).
 * This service materializes those new mismatches into the gateway-agnostic
 * reconciliation_runs + reconciliation_items tables (V25).
 */
@Service
public class LegacyMismatchRunMaterializer {

    private final ReconciliationRunRepository runRepository;
    private final ReconciliationItemRepository itemRepository;
    private final ReconciliationMismatchRepository mismatchRepository;

    public LegacyMismatchRunMaterializer(
            ReconciliationRunRepository runRepository,
            ReconciliationItemRepository itemRepository,
            ReconciliationMismatchRepository mismatchRepository) {
        this.runRepository = runRepository;
        this.itemRepository = itemRepository;
        this.mismatchRepository = mismatchRepository;
    }

    @Transactional
    public ReconciliationRun materialize(String gatewayType, LocalDate periodStart, LocalDate periodEnd, LocalDateTime mismatchesCreatedAfter) {
        ReconciliationRun run = new ReconciliationRun();
        run.setGatewayType(gatewayType);
        run.setPeriodStart(periodStart);
        run.setPeriodEnd(periodEnd);
        run.setStatus(ReconciliationRunStatus.STARTED);
        run = runRepository.save(run);

        List<com.ecommerce.payment.domain.ReconciliationMismatch> newMismatches = mismatchRepository
                .findByResolvedFalseAndCreatedAtAfterOrderByCreatedAtDesc(mismatchesCreatedAfter);

        for (var m : newMismatches) {
            ReconciliationCategory category = ReconciliationCategory.fromLegacyMismatchType(m.getMismatchType());
            ReconciliationSeverity severity = ReconciliationSeverity.defaultFor(category);

            ReconciliationItem item = new ReconciliationItem();
            item.setRunId(run.getId());
            item.setCategory(category);
            item.setSeverity(severity);
            item.setProviderRef(m.getProviderTransactionId());
            item.setInternalRef(m.getOrderId());
            item.setExpectedAmount(m.getInternalAmount());
            item.setActualAmount(m.getProviderAmount());
            item.setCurrency("INR");
            item.setStatus(ReconciliationItemStatus.OPEN);
            item.setNotes(m.getMismatchType());

            itemRepository.save(item);
        }

        run.setStatus(ReconciliationRunStatus.COMPLETED);
        run.setFinishedAt(LocalDateTime.now());
        run.setSummary("{\"items_created\":" + newMismatches.size() + "}");
        return runRepository.save(run);
    }
}
