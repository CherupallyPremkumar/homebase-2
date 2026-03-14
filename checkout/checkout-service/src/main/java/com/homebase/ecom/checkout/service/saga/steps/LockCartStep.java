package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.CartClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockCartStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(LockCartStep.class);
    private final CartClient cartClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(1000).backoffMultiplier(2.0).build();

    public LockCartStep(CartClient cartClient) {
        this.cartClient = cartClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing LockCartStep for cart: {}", checkout.getCartId());
        try {
            CartSnapshot snapshot = cartClient.lockCart(checkout.getCartId());
            checkout.setLockedCart(snapshot);
            return SagaStepResult.builder().stepName(getStepName()).success(true).data(snapshot).build();
        } catch (Exception e) {
            log.error("Failed to lock cart", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        log.info("Compensating LockCartStep for cart: {}", checkout.getCartId());
        try {
            cartClient.unlockCart(checkout.getCartId());
        } catch (Exception e) {
            log.error("Failed to unlock cart during compensation", e);
        }
    }

    @Override
    public String getStepName() { return "LockCartStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
