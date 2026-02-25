package com.ecommerce.payment.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.homebase.ecom.shared.model.Money;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "gateway_charge_id")
    private String gatewayChargeId;

    @Column(name = "gateway_event_id", unique = true)
    private String gatewayEventId;

    @Column(nullable = false, length = 50)
    private String status; // SUCCEEDED, FAILED, REFUNDED

    @Column(name = "gateway_type", length = 20)
    private String gatewayType; // STRIPE, RAZORPAY

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money amount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "stripe_fee")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money stripeFee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "net_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money netAmount;

    @Column(name = "payment_method_type", length = 50)
    private String paymentMethodType;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String s) {
        this.gatewayTransactionId = s;
    }

    public String getGatewayChargeId() {
        return gatewayChargeId;
    }

    public void setGatewayChargeId(String gatewayChargeId) {
        this.gatewayChargeId = gatewayChargeId;
    }

    public String getGatewayEventId() {
        return gatewayEventId;
    }

    public void setGatewayEventId(String gatewayEventId) {
        this.gatewayEventId = gatewayEventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Money getStripeFee() {
        return stripeFee;
    }

    public void setStripeFee(Money stripeFee) {
        this.stripeFee = stripeFee;
    }

    public Money getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Money netAmount) {
        this.netAmount = netAmount;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }
}
