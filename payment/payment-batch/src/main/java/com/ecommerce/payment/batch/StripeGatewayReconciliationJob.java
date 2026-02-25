package com.ecommerce.payment.batch;

import com.ecommerce.payment.domain.ReconciliationMismatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripeGatewayReconciliationJob implements GatewayReconciliationJob {

    private final StripeReconciliationService stripeReconciliationService;

    public StripeGatewayReconciliationJob(StripeReconciliationService stripeReconciliationService) {
        this.stripeReconciliationService = stripeReconciliationService;
    }

    @Override
    public String gatewayType() {
        return "stripe";
    }

    @Override
    public void triggerReconciliationJob() {
        stripeReconciliationService.triggerReconciliationJob();
    }

    @Override
    public List<ReconciliationMismatch> getUnresolvedMismatches(String mismatchType, String orderId, String providerTransactionId) {
        // providerTransactionId maps to Stripe charge id (ch_*) in the legacy model.
        return stripeReconciliationService.getUnresolvedMismatches(mismatchType, orderId, providerTransactionId);
    }
}
