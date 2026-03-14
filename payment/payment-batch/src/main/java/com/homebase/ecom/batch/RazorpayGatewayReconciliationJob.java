package com.homebase.ecom.batch;

import com.homebase.ecom.payment.domain.ReconciliationMismatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RazorpayGatewayReconciliationJob implements GatewayReconciliationJob {

    private final RazorpayReconciliationService razorpayReconciliationService;

    public RazorpayGatewayReconciliationJob(RazorpayReconciliationService razorpayReconciliationService) {
        this.razorpayReconciliationService = razorpayReconciliationService;
    }

    @Override
    public String gatewayType() {
        return "razorpay";
    }

    @Override
    public void triggerReconciliationJob() {
        razorpayReconciliationService.triggerReconciliationJob();
    }

    @Override
    public List<ReconciliationMismatch> getUnresolvedMismatches(String mismatchType, String orderId,
            String providerTransactionId) {
        return razorpayReconciliationService.getUnresolvedMismatches(mismatchType, orderId, providerTransactionId);
    }
}
