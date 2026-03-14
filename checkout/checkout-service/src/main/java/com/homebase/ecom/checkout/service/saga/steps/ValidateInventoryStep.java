package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.InventoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateInventoryStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(ValidateInventoryStep.class);
    private final InventoryClient inventoryClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(500).backoffMultiplier(2.0).build();

    public ValidateInventoryStep(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing ValidateInventoryStep for cart: {}", checkout.getCartId());
        try {
            inventoryClient.reserveInventory(checkout.getCartId());
            return SagaStepResult.builder().stepName(getStepName()).success(true).build();
        } catch (Exception e) {
            log.error("Inventory reservation failed", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        log.info("Compensating ValidateInventoryStep for cart: {}", checkout.getCartId());
        try {
            inventoryClient.releaseInventory(checkout.getCartId());
        } catch (Exception e) {
            log.error("Failed to release inventory", e);
        }
    }

    @Override
    public String getStepName() { return "ValidateInventoryStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
