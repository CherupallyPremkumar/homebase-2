package com.homebase.ecom.checkout.dto;

/**
 * DTO for checkout responses (used by Chenile Proxy clients).
 */
public class CheckoutDto {
    public String id;
    public String customerId;
    public String cartId;
    public String orderId;
    public String paymentId;
    public String lastCompletedStep;
    public String failureReason;
    public String state;
}
