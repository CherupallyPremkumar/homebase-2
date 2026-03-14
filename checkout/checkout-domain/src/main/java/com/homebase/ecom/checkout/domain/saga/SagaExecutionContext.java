package com.homebase.ecom.checkout.domain.saga;

import com.homebase.ecom.checkout.domain.model.Checkout;
import java.util.List;
import java.util.Map;

/**
 * Execution context (shared across steps)
 */
public class SagaExecutionContext {
    private final String sagaId;
    private final Checkout checkout;
    private final Map<String, Object> data;
    private final List<SagaStepResult> results;

    public SagaExecutionContext(String sagaId, Checkout checkout, Map<String, Object> data, List<SagaStepResult> results) {
        this.sagaId = sagaId;
        this.checkout = checkout;
        this.data = data;
        this.results = results;
    }

    public String getSagaId() { return sagaId; }
    public Checkout getCheckout() { return checkout; }
    public Map<String, Object> getData() { return data; }
    public List<SagaStepResult> getResults() { return results; }
}
