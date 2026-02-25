package com.ecommerce.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminPaymentDetailDto {
    private String paymentId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String gatewayType;
    private String gatewayTransactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Customer Info (Local)
    private String customerId;
    private String customerName;
    private String customerEmail;

    // Payout Linkage (Local DB)
    private List<PayoutLinkDto> payoutLinks;

    // Ledger Info (Local)
    private List<LedgerEntryDto> ledgerEntries;
    private String reconciliationStatus;

    // Webhook Logs (Local Logs)
    private List<Map<String, Object>> webhookLogs;

    @Data
    @Builder
    public static class PayoutLinkDto {
        private String payoutId;
        private String providerPayoutId;
        private String payoutStatus;
        private LocalDateTime payoutAt;

        private String payoutLineId;
        private String payoutLineType;
        private String providerBalanceTxnId;

        private BigDecimal grossAmount;
        private BigDecimal feeAmount;
        private BigDecimal netAmount;
        private String currency;
    }

    @Data
    @Builder
    public static class LedgerEntryDto {
        private String id;
        private String accountName;
        private String type; // DEBIT/CREDIT
        private BigDecimal amount;
        private LocalDateTime timestamp;
    }
}
