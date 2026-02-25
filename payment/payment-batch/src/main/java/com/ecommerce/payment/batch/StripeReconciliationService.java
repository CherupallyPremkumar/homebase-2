package com.ecommerce.payment.batch;

import com.ecommerce.payment.batch.StripeSettlementDownloader;
import com.ecommerce.payment.domain.ReconciliationMismatch;
import com.ecommerce.payment.repository.ReconciliationMismatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StripeReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(StripeReconciliationService.class);

    private final JobLauncher jobLauncher;
    private final Job stripeReconciliationJob;
    private final StripeSettlementDownloader downloader;
    private final ReconciliationMismatchRepository mismatchRepository;

    public StripeReconciliationService(JobLauncher jobLauncher,
            Job stripeReconciliationJob,
            StripeSettlementDownloader downloader,
            ReconciliationMismatchRepository mismatchRepository) {
        this.jobLauncher = jobLauncher;
        this.stripeReconciliationJob = stripeReconciliationJob;
        this.downloader = downloader;
        this.mismatchRepository = mismatchRepository;
    }

    public void triggerReconciliationJob() {
        try {
            // 1. Download the latest CSV
            String csvPath = downloader.downloadLatestSettlementCsv();

            // 2. Prepare job parameters
            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", csvPath)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // 3. Launch job
            log.info("Launching stripeReconciliationJob...");
            jobLauncher.run(stripeReconciliationJob, params);
            log.info("stripeReconciliationJob completed successfully.");

        } catch (Exception e) {
            log.error("Failed to run stripeReconciliationJob", e);
            throw new RuntimeException("Reconciliation job failed", e);
        }
    }

    public List<ReconciliationMismatch> getUnresolvedMismatches() {
        return getUnresolvedMismatches(null, null, null);
    }

    public List<ReconciliationMismatch> getUnresolvedMismatches(String mismatchType, String orderId, String stripeChargeId) {
        // Note: stripeChargeId is a legacy parameter name; schema is now provider_transaction_id.
        List<ReconciliationMismatch> mismatches = mismatchRepository.findByResolvedFalseOrderByCreatedAtDesc();

        return mismatches.stream()
                .filter(m -> !StringUtils.hasText(mismatchType) || mismatchType.equalsIgnoreCase(m.getMismatchType()))
                .filter(m -> !StringUtils.hasText(orderId) || orderId.equalsIgnoreCase(m.getOrderId()))
                .filter(m -> !StringUtils.hasText(stripeChargeId) || stripeChargeId.equalsIgnoreCase(m.getProviderTransactionId()))
                .toList();
    }

    @Transactional
    public void resolveMismatch(String mismatchId, String resolutionNotes, String resolvedBy) {
        ReconciliationMismatch mismatch = mismatchRepository.findById(mismatchId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown mismatchId: " + mismatchId));

        if (mismatch.isResolved()) {
            return;
        }

        mismatch.setResolved(true);
        mismatch.setResolvedAt(LocalDateTime.now());
        mismatch.setResolvedBy(StringUtils.hasText(resolvedBy) ? resolvedBy.trim() : null);
        mismatch.setResolutionNotes(StringUtils.hasText(resolutionNotes) ? resolutionNotes.trim() : null);

        mismatchRepository.save(mismatch);
    }
}
