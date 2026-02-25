package com.ecommerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentTransactionDto {
    private String paymentId;
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String gatewayType;
    private String gatewayTransactionId;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String failureReason;
    private String metadata;
}
