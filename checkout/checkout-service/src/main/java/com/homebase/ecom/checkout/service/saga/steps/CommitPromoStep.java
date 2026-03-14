package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.PromoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommitPromoStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(CommitPromoStep.class);
    private final PromoClient promoClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(1000).build();

    public CommitPromoStep(PromoClient promoClient) {
        this.promoClient = promoClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing CommitPromoStep for user: {}", checkout.getUserId());
        String couponCode = (String) context.getData().get("couponCode");
        if (couponCode == null || couponCode.isEmpty()) return SagaStepResult.builder().stepName(getStepName()).success(true).build();
        
        try {
            promoClient.commitPromo(couponCode, checkout.getUserId());
            return SagaStepResult.builder().stepName(getStepName()).success(true).build();
        } catch (Exception e) {
            log.error("Failed to commit promo", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        String couponCode = (String) context.getData().get("couponCode");
        if (couponCode == null || couponCode.isEmpty()) return;
        try {
            promoClient.releasePromo(couponCode, checkout.getUserId());
        } catch (Exception e) {
            log.error("Failed to release promo", e);
        }
    }

    @Override
    public String getStepName() { return "CommitPromoStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
