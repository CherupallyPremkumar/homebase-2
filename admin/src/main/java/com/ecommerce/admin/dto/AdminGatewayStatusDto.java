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
public class AdminGatewayStatusDto {
    private String gateway;
    private boolean healthy;
    private long totalTransactions;
    private long todayTransactions;
    private double failureRate;
    private BigDecimal totalAmount;
    private double uptime;
    private LocalDateTime lastChecked;
    private String message;
}
