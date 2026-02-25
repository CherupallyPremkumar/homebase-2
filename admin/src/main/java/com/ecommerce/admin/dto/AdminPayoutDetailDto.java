package com.ecommerce.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminPayoutDetailDto {
    private String payoutId;
    private String gatewayType;
    private String providerPayoutId;
    private String status;
    private LocalDateTime payoutAt;
    private String currency;
    private BigDecimal grossAmount;
    private BigDecimal feeAmount;
    private BigDecimal netAmount;
    private LocalDateTime createdAt;

    // Coverage metrics (derived from payout_lines)
    private long totalLines;
    private long matchedLines;
    private long issuesCount;
    /**
     * 0-100 percentage.
     */
    private BigDecimal matchedPercent;

    private List<PayoutLineDto> lines;

    @Data
    @Builder
    public static class PayoutLineDto {
        private String id;
        private String lineType;
        private String providerBalanceTxnId;
        private String internalPaymentTransactionId;
        private String internalRefundId;
        private BigDecimal grossAmount;
        private BigDecimal feeAmount;
        private BigDecimal netAmount;
        private String currency;
        private LocalDateTime occurredAt;
    }
}
