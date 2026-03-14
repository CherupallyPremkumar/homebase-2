package com.homebase.ecom.checkout.domain.saga;

import com.homebase.ecom.checkout.domain.model.Checkout;

/**
 * Saga Step Interface
 * Each step represents one service call
 */
public interface SagaStep {
    
    /**
     * Execute the step
     */
    SagaStepResult execute(Checkout checkout, SagaExecutionContext context);
    
    /**
     * Compensate if step fails (undo)
     */
    void compensate(Checkout checkout, SagaExecutionContext context);
    
    /**
     * Get step name
     */
    String getStepName();
    
    /**
     * Is this step critical? (failure means abort)
     */
    default boolean isCritical() { return true; }
    
    /**
     * Retry policy
     */
    RetryPolicy getRetryPolicy();
}
