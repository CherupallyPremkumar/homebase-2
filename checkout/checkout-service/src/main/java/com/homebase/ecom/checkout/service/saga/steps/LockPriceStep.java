package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.PriceSnapshot;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.PricingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockPriceStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(LockPriceStep.class);
    private final PricingClient pricingClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(1000).backoffMultiplier(2.0).build();

    public LockPriceStep(PricingClient pricingClient) {
        this.pricingClient = pricingClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing LockPriceStep for cart: {}", checkout.getCartId());
        String couponCode = (String) context.getData().get("couponCode");
        try {
            PriceSnapshot snapshot = pricingClient.lockPrice(checkout, couponCode);
            checkout.setLockedPrice(snapshot);
            return SagaStepResult.builder().stepName(getStepName()).success(true).data(snapshot).build();
        } catch (Exception e) {
            log.error("Failed to lock price", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        // Price locks are often time-based and expire automatically, but we could add explicit release if needed.
    }

    @Override
    public String getStepName() { return "LockPriceStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
