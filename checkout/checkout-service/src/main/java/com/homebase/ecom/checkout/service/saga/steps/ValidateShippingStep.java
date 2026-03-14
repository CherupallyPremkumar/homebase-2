package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.ShippingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateShippingStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(ValidateShippingStep.class);
    private final ShippingClient shippingClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(1).initialDelayMs(500).build();

    public ValidateShippingStep(ShippingClient shippingClient) {
        this.shippingClient = shippingClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing ValidateShippingStep for checkout: {}", checkout.getCheckoutId());
        try {
            // Shipping address would normally come from the checkout request stored in context
            boolean isValid = shippingClient.validateAddress(null); // Passing null for now as address logic is WIP
            if (!isValid) {
                return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage("Invalid shipping address").build();
            }
            return SagaStepResult.builder().stepName(getStepName()).success(true).build();
        } catch (Exception e) {
            log.error("Shipping validation failed", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        // No compensation for validation
    }

    @Override
    public String getStepName() { return "ValidateShippingStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
