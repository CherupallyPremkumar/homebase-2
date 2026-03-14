package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.CheckoutState;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.PaymentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitiatePaymentStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(InitiatePaymentStep.class);
    private final PaymentClient paymentClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(1000).build();

    public InitiatePaymentStep(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing InitiatePaymentStep for checkout: {}", checkout.getCheckoutId());
        try {
            paymentClient.initiatePayment(checkout);
            checkout.transitionTo(CheckoutState.PAYMENT_PENDING);
            return SagaStepResult.builder().stepName(getStepName()).success(true).build();
        } catch (Exception e) {
            log.error("Failed to initiate payment", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        // Payment initiation usually creates a pending intent that can be cancelled or left to expire.
    }

    @Override
    public String getStepName() { return "InitiatePaymentStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
