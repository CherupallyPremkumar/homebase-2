package com.homebase.ecom.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Platform-wide REST exception handler for HomeBase.
 *
 * <p>
 * Converts all domain and policy exceptions into consistent
 * {@link ErrorResponse} JSON envelopes with correct HTTP status codes.
 *
 * <h3>Status Mapping</h3>
 * <ul>
 * <li>422 — {@link PolicyViolationException} subclasses (business rule
 * constraints)</li>
 * <li>400 — {@link IllegalArgumentException} (invalid input / argument)</li>
 * <li>409 — {@link IllegalStateException} (invalid state transition)</li>
 * <li>403 — {@link SecurityException} (access denied)</li>
 * <li>500 — Everything else (prevents stack trace leakage)</li>
 * </ul>
 *
 * <p>
 * Registered automatically via
 * {@code SharedServiceConfiguration @ComponentScan}
 * over the {@code com.homebase.ecom.shared} package.
 *
 * <p>
 * <strong>Module Boundary Note:</strong> This handler does NOT import any
 * module-specific exception class. It catches only the abstract
 * {@link PolicyViolationException} base class defined in {@code shared-api},
 * so no upward dependency is introduced.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===================================================================
    // 422 UNPROCESSABLE ENTITY — All policy violations
    // ===================================================================

    /**
     * Catches any exception that extends {@link PolicyViolationException}.
     * Each module's specific exception (e.g. {@code OrderValueTooLowException},
     * {@code CancellationWindowExpiredException},
     * {@code InvalidCatalogTagException})
     * extends this base class — so this single handler covers all 12 modules.
     *
     * <p>
     * The {@code module} field is populated from
     * {@link PolicyViolationException#getModule()} to identify the originating
     * context.
     */
    @ExceptionHandler(PolicyViolationException.class)
    public ResponseEntity<ErrorResponse> handlePolicyViolation(PolicyViolationException ex) {
        log.warn("[POLICY:{}] {}", ex.getModule(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(422, "Policy Violation", ex.getMessage(), ex.getModule()));
    }

    // ===================================================================
    // 400 BAD REQUEST — Validation errors
    // ===================================================================

    /**
     * Catches {@link IllegalArgumentException} for any invalid input not covered
     * by a typed policy exception (e.g. missing required field, bad format).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadArgument(IllegalArgumentException ex) {
        log.warn("[VALIDATION] {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Bad Request", ex.getMessage(), null));
    }

    // ===================================================================
    // 409 CONFLICT — Illegal state transitions
    // ===================================================================

    /**
     * Catches {@link IllegalStateException} for invalid aggregate state transitions
     * (e.g. trying to cancel an already-shipped order).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        log.warn("[STATE] {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Conflict", ex.getMessage(), null));
    }

    // ===================================================================
    // 403 FORBIDDEN — Security violations
    // ===================================================================

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurity(SecurityException ex) {
        log.warn("[SECURITY] Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Forbidden", "Access denied.", null));
    }

    // ===================================================================
    // 500 INTERNAL SERVER ERROR — Unexpected failures
    // ===================================================================

    /**
     * Catch-all handler. Prevents internal stack traces from leaking to clients.
     * Logs the full exception for ops investigation.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("[UNHANDLED] Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(500, "Internal Server Error",
                        "An unexpected error occurred. Please try again or contact support."));
    }
}
