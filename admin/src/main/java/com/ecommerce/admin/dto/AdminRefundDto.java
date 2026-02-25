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
public class AdminRefundDto {
    private String refundId;
    private String paymentId;
    private BigDecimal amount;
    private String reason;
    private String status; // PENDING, APPROVED, PROCESSING, COMPLETED, FAILED
    private String notes;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String gatewayRefundId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
