package com.ecommerce.payment.batch;

import com.ecommerce.payment.domain.ReconciliationMismatch;
import com.ecommerce.payment.repository.ReconciliationMismatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GatewayReconciliationDispatcher {

    private static final Logger log = LoggerFactory.getLogger(GatewayReconciliationDispatcher.class);

    private static final String ACTIVE_GATEWAY_KEY = "payment:gateway:active";
    private static final String DEFAULT_GATEWAY = "stripe";

    private final List<GatewayReconciliationJob> gatewayJobs;
    private final ReconciliationMismatchRepository mismatchRepository;
    private final StringRedisTemplate redisTemplate;
    private final LegacyMismatchRunMaterializer legacyMismatchRunMaterializer;

    public GatewayReconciliationDispatcher(
            List<GatewayReconciliationJob> gatewayJobs,
            ReconciliationMismatchRepository mismatchRepository,
            StringRedisTemplate redisTemplate,
            LegacyMismatchRunMaterializer legacyMismatchRunMaterializer) {
        this.gatewayJobs = gatewayJobs;
        this.mismatchRepository = mismatchRepository;
        this.redisTemplate = redisTemplate;
        this.legacyMismatchRunMaterializer = legacyMismatchRunMaterializer;
    }

    public String getActiveGatewayType() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ACTIVE_GATEWAY_KEY))
                .filter(StringUtils::hasText)
                .orElse(DEFAULT_GATEWAY);
    }

    public List<String> getSupportedGatewayTypes() {
        return gatewayJobs.stream()
                .map(GatewayReconciliationJob::gatewayType)
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public void triggerReconciliationJob(String gatewayType) {
        String resolvedGatewayType = resolveGatewayType(gatewayType);

        Optional<GatewayReconciliationJob> job = jobForOptional(resolvedGatewayType);
        if (job.isEmpty()) {
            log.warn("No reconciliation job registered for gatewayType={}. Skipping.", resolvedGatewayType);
            return;
        }

        // Track time to materialize only newly created mismatches for this run.
        LocalDateTime startedAt = LocalDateTime.now();

        job.get().triggerReconciliationJob();

        // Bridge until gateway-agnostic reconciliation runs/items are produced directly.
        // Legacy reconciliation_mismatches is Stripe-specific.
        if ("stripe".equalsIgnoreCase(resolvedGatewayType)) {
            legacyMismatchRunMaterializer.materialize(
                    resolvedGatewayType,
                    java.time.LocalDate.now(),
                    java.time.LocalDate.now(),
                    startedAt);
        }
    }

    public List<ReconciliationMismatch> getUnresolvedMismatches(
            String gatewayType,
            String mismatchType,
            String orderId,
            String providerTransactionId) {
        return jobFor(resolveGatewayType(gatewayType)).getUnresolvedMismatches(mismatchType, orderId, providerTransactionId);
    }

    @Transactional
    public void resolveMismatch(String mismatchId, String resolutionNotes) {
        resolveMismatch(mismatchId, resolutionNotes, null);
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

    private String resolveGatewayType(String gatewayType) {
        return StringUtils.hasText(gatewayType) ? gatewayType.trim().toLowerCase() : getActiveGatewayType();
    }

    private Optional<GatewayReconciliationJob> jobForOptional(String gatewayType) {
        return gatewayJobs.stream()
                .filter(j -> j.gatewayType().equalsIgnoreCase(gatewayType))
                .findFirst();
    }

    private GatewayReconciliationJob jobFor(String gatewayType) {
        return jobForOptional(gatewayType)
                .orElseThrow(() -> new IllegalArgumentException("No reconciliation job registered for gatewayType=" + gatewayType));
    }
}
