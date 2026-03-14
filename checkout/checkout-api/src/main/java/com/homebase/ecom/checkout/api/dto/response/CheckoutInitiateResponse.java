package com.homebase.ecom.checkout.api.dto.response;

import com.homebase.ecom.checkout.api.dto.*;

import java.time.LocalDateTime;

/**
 * Response DTO for checkout initiation
 */
public class CheckoutInitiateResponse {

    private boolean success;
    private String checkoutId;
    private String orderId;
    private String status;

    private PaymentResponseDTO payment;
    private OrderResponseDTO order;
    private LocksResponseDTO locks;

    private String sagaExecutionId;
    private String nextStep;
    private String redirectUrl;

    private ErrorDTO error;
    private CompensationResponseDTO compensation;

    private LocalDateTime timestamp;

    // Factory methods
    public static CheckoutInitiateResponse success(
            String checkoutId,
            String orderId,
            String status,
            PaymentResponseDTO payment,
            OrderResponseDTO order,
            LocksResponseDTO locks,
            String sagaExecutionId) {
        CheckoutInitiateResponse response = new CheckoutInitiateResponse();
        response.success = true;
        response.checkoutId = checkoutId;
        response.orderId = orderId;
        response.status = status;
        response.payment = payment;
        response.order = order;
        response.locks = locks;
        response.sagaExecutionId = sagaExecutionId;
        response.nextStep = "COMPLETE_PAYMENT";
        response.redirectUrl = payment != null ? payment.getPaymentUrl() : null;
        response.timestamp = LocalDateTime.now();
        return response;
    }

    public static CheckoutInitiateResponse error(String errorMessage) {
        CheckoutInitiateResponse response = new CheckoutInitiateResponse();
        response.success = false;
        response.status = "FAILED";
        response.error = new ErrorDTO("CHECKOUT_FAILED", errorMessage, null);
        response.timestamp = LocalDateTime.now();
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentResponseDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponseDTO payment) {
        this.payment = payment;
    }

    public OrderResponseDTO getOrder() {
        return order;
    }

    public void setOrder(OrderResponseDTO order) {
        this.order = order;
    }

    public LocksResponseDTO getLocks() {
        return locks;
    }

    public void setLocks(LocksResponseDTO locks) {
        this.locks = locks;
    }

    public String getSagaExecutionId() {
        return sagaExecutionId;
    }

    public void setSagaExecutionId(String sagaExecutionId) {
        this.sagaExecutionId = sagaExecutionId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public ErrorDTO getError() {
        return error;
    }

    public void setError(ErrorDTO error) {
        this.error = error;
    }

    public CompensationResponseDTO getCompensation() {
        return compensation;
    }

    public void setCompensation(CompensationResponseDTO compensation) {
        this.compensation = compensation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
