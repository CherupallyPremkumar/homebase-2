package com.homebase.ecom.checkout.api.dto.response;

import com.homebase.ecom.checkout.api.dto.OrderResponseDTO;
import com.homebase.ecom.checkout.api.dto.PaymentResponseDTO;
import com.homebase.ecom.checkout.api.dto.StatusTimelineDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for checkout status
 */
public class CheckoutStatusResponse {

    private String checkoutId;
    private String orderId;
    private String status;
    private String paymentStatus;

    private OrderResponseDTO order;
    private PaymentResponseDTO payment;

    private List<StatusTimelineDTO> timeline;

    private Integer nextPollInterval;
    private LocalDateTime estimatedCompletionTime;
    private LocalDateTime completedAt;

    // Getters and Setters
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderResponseDTO getOrder() {
        return order;
    }

    public void setOrder(OrderResponseDTO order) {
        this.order = order;
    }

    public PaymentResponseDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponseDTO payment) {
        this.payment = payment;
    }

    public List<StatusTimelineDTO> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<StatusTimelineDTO> timeline) {
        this.timeline = timeline;
    }

    public Integer getNextPollInterval() {
        return nextPollInterval;
    }

    public void setNextPollInterval(Integer nextPollInterval) {
        this.nextPollInterval = nextPollInterval;
    }

    public LocalDateTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(LocalDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
