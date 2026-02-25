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
public class AdminPayoutDto {
    private String payoutId;
    private String gatewayType;
    private String providerPayoutId;
    private String status;
    private LocalDateTime payoutAt;
    private String currency;
    private BigDecimal grossAmount;
    private BigDecimal feeAmount;
    private BigDecimal netAmount;

    // Coverage metrics (derived from payout_lines)
    private long totalLines;
    private long matchedLines;
    private long issuesCount;
    /**
     * 0-100 percentage.
     */
    private BigDecimal matchedPercent;
}
