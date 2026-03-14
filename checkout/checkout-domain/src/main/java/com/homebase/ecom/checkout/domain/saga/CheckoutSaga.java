package com.homebase.ecom.checkout.domain.saga;

import java.util.List;
import java.util.UUID;

/**
 * Checkout Saga Definition
 * Defines the sequence of steps and compensation logic
 */
public class CheckoutSaga {

    private final String sagaId;
    private final String checkoutId;
    private final List<SagaStep> steps;

    public CheckoutSaga(String checkoutId, List<SagaStep> steps) {
        this.sagaId = UUID.randomUUID().toString();
        this.checkoutId = checkoutId;
        this.steps = steps;
    }

    public String getSagaId() {
        return sagaId;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public List<SagaStep> getSteps() {
        return steps;
    }
}
