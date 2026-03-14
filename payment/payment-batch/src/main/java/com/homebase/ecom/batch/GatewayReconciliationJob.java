package com.homebase.ecom.batch;

import com.homebase.ecom.payment.domain.ReconciliationMismatch;

import java.util.List;

/**
 * Gateway-specific reconciliation entrypoint used by the gateway-agnostic dispatcher.
 */
public interface GatewayReconciliationJob {

    /**
     * @return the gateway type this job supports (e.g., "stripe", "razorpay").
     */
    String gatewayType();

    /**
     * Trigger a reconciliation run for this gateway.
     */
    void triggerReconciliationJob();

    /**
     * Fetch unresolved mismatches for this gateway.
     *
     * For now this is backed by the legacy reconciliation_mismatches table.
     */
    List<ReconciliationMismatch> getUnresolvedMismatches(String mismatchType, String orderId, String providerTransactionId);
}
