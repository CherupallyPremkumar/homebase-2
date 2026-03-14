package com.homebase.ecom.shared.exception;

/**
 * Base exception for all platform policy violations in HomeBase.
 *
 * <p>
 * Every module's specific policy exception (e.g.
 * {@code OrderValueTooLowException},
 * {@code CancellationWindowExpiredException}) extends this class.
 *
 * <p>
 * This design allows {@link GlobalExceptionHandler} in {@code shared-service}
 * to catch all policy violations with a single handler, returning HTTP 422,
 * without requiring cross-module imports.
 *
 * <h3>Module convention</h3>
 * Subclasses should provide the {@code module} field to identify which
 * bounded context raised the violation:
 * 
 * <pre>
 * public class OrderValueTooLowException extends PolicyViolationException {
 *     public OrderValueTooLowException(double actual, double min) {
 *         super("checkout", "Order value ₹" + actual + " is below minimum ₹" + min);
 *     }
 * }
 * </pre>
 */
public abstract class PolicyViolationException extends RuntimeException {

    private final String module;

    protected PolicyViolationException(String module, String message) {
        super(message);
        this.module = module;
    }

    protected PolicyViolationException(String module, String message, Throwable cause) {
        super(message, cause);
        this.module = module;
    }

    /**
     * The bounded context / module that raised this policy violation.
     * Used by {@link GlobalExceptionHandler} to populate the {@code module} field
     * in the {@link ErrorResponse}.
     */
    public String getModule() {
        return module;
    }
}
