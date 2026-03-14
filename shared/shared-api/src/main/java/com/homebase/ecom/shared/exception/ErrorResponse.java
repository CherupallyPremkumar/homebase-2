package com.homebase.ecom.shared.exception;

import java.time.Instant;

/**
 * Structured error response envelope for all REST API errors in the HomeBase
 * platform.
 *
 * <p>
 * Returned by {@code GlobalExceptionHandler} for every non-2xx response.
 * Provides a consistent shape regardless of the originating module.
 *
 * <pre>
 * {
 *   "status": 422,
 *   "error": "Policy Violation",
 *   "message": "Order value ₹450.0 is below the minimum required ₹500.0.",
 *   "module": "checkout",
 *   "timestamp": "2026-03-07T04:48:00Z"
 * }
 * </pre>
 */
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String module;
    private final String timestamp;

    public ErrorResponse(int status, String error, String message, String module) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.module = module;
        this.timestamp = Instant.now().toString();
    }

    /** Convenience factory when the module is not known. */
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, null);
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getModule() {
        return module;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
