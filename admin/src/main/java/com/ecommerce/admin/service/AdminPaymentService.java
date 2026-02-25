package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminGatewayStatusDto;
import com.ecommerce.admin.dto.AdminPayoutDetailDto;
import com.ecommerce.admin.dto.AdminPayoutDto;
import com.ecommerce.admin.dto.AdminPaymentDetailDto;
import com.ecommerce.admin.dto.AdminPaymentTransactionDto;
import com.ecommerce.payment.domain.LedgerEntry;
import com.ecommerce.payment.domain.Payout;
import com.ecommerce.payment.domain.PayoutLine;
import com.ecommerce.payment.domain.PaymentTransaction;
import com.ecommerce.payment.domain.Refund;
import com.ecommerce.payment.repository.LedgerEntryRepository;
import com.ecommerce.payment.repository.PayoutLineRepository;
import com.ecommerce.payment.repository.PayoutRepository;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
import com.ecommerce.payment.repository.RefundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminPaymentService {

    private final PaymentTransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final PayoutRepository payoutRepository;
    private final PayoutLineRepository payoutLineRepository;
    private final RefundRepository refundRepository;
    private final AuditService auditService;

    public AdminPaymentService(PaymentTransactionRepository transactionRepository,
            LedgerEntryRepository ledgerEntryRepository,
            PayoutRepository payoutRepository,
            PayoutLineRepository payoutLineRepository,
            RefundRepository refundRepository,
            AuditService auditService) {
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.payoutRepository = payoutRepository;
        this.payoutLineRepository = payoutLineRepository;
        this.refundRepository = refundRepository;
        this.auditService = auditService;
    }

    // ── List & Search ──────────────────────────────────────────────

    public Page<AdminPaymentTransactionDto> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public AdminPaymentTransactionDto getTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));
    }

    // ── Payment Detail View (Local Data Only) ─────────────────────

    public AdminPaymentDetailDto getPaymentDetail(String transactionId) {
        PaymentTransaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

        List<LedgerEntry> entries = ledgerEntryRepository.findByTransactionId(transactionId);

        List<AdminPaymentDetailDto.LedgerEntryDto> ledgerDtos = entries.stream()
                .map(e -> AdminPaymentDetailDto.LedgerEntryDto.builder()
                        .id(e.getId())
                        .accountName(e.getLedgerAccountId())
                        .type(e.getAmount().signum() >= 0 ? "DEBIT" : "CREDIT")
                        .amount(e.getAmount().abs())
                        .timestamp(e.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        List<AdminPaymentDetailDto.PayoutLinkDto> payoutLinks = buildPayoutLinks(transactionId);

        String gatewayType = resolveGatewayType(tx);

        return AdminPaymentDetailDto.builder()
                .paymentId(tx.getId())
                .orderId(tx.getOrderId())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .status(tx.getStatus())
                .gatewayType(gatewayType)
                .gatewayTransactionId(tx.getGatewayTransactionId())
                .createdAt(tx.getCreatedAt())
                .payoutLinks(payoutLinks)
                .ledgerEntries(ledgerDtos)
                .reconciliationStatus(ledgerDtos.isEmpty() ? "PENDING" : "MATCHED")
                .build();
    }

    private List<AdminPaymentDetailDto.PayoutLinkDto> buildPayoutLinks(String transactionId) {
        List<PayoutLine> lines = payoutLineRepository.findByInternalPaymentTransactionId(transactionId);
        if (lines == null || lines.isEmpty()) {
            return List.of();
        }

        List<String> payoutIds = lines.stream().map(PayoutLine::getPayoutId).distinct().toList();
        Map<String, Payout> payoutById = payoutRepository.findAllById(payoutIds).stream()
                .collect(Collectors.toMap(Payout::getId, p -> p));

        return lines.stream()
                .map(l -> {
                    Payout payout = payoutById.get(l.getPayoutId());
                    return AdminPaymentDetailDto.PayoutLinkDto.builder()
                            .payoutId(l.getPayoutId())
                            .providerPayoutId(payout != null ? payout.getProviderPayoutId() : null)
                            .payoutStatus(payout != null ? payout.getStatus() : null)
                            .payoutAt(payout != null ? payout.getPayoutAt() : null)
                            .payoutLineId(l.getId())
                            .payoutLineType(l.getLineType())
                            .providerBalanceTxnId(l.getProviderBalanceTxnId())
                            .grossAmount(l.getGrossAmount())
                            .feeAmount(l.getFeeAmount())
                            .netAmount(l.getNetAmount())
                            .currency(l.getCurrency())
                            .build();
                })
                .toList();
    }

    // ── Payouts (Local DB) ───────────────────────────────────────

    @Transactional
    public void linkPayoutLine(
            String payoutId,
            String payoutLineId,
            String internalPaymentTransactionId,
            String internalRefundId,
            String performedBy,
            String notes) {
        boolean hasPayment = internalPaymentTransactionId != null && !internalPaymentTransactionId.isBlank();
        boolean hasRefund = internalRefundId != null && !internalRefundId.isBlank();

        if (hasPayment == hasRefund) {
            // either both true, or both false
            throw new IllegalArgumentException("INVALID_LINK_TARGET");
        }

        PayoutLine line = payoutLineRepository.findById(payoutLineId)
                .orElseThrow(() -> new IllegalArgumentException("UNKNOWN_PAYOUT_LINE"));

        if (!payoutId.equals(line.getPayoutId())) {
            throw new IllegalArgumentException("LINE_NOT_IN_PAYOUT");
        }

        String trimmedNotes = (notes != null && !notes.isBlank()) ? notes.trim() : null;

        if (hasPayment) {
            String txId = internalPaymentTransactionId.trim();
            if (!transactionRepository.existsById(txId)) {
                throw new IllegalArgumentException("UNKNOWN_PAYMENT_TRANSACTION");
            }

            line.setInternalPaymentTransactionId(txId);
            line.setInternalRefundId(null);
            payoutLineRepository.save(line);

            auditService.logAction(
                    "PAYOUT_LINE_LINKED",
                    "PayoutLine",
                    payoutLineId,
                    performedBy,
                    "payoutId=" + payoutId + ", linkedPaymentTransactionId=" + txId + (trimmedNotes != null ? ", notes=" + trimmedNotes : ""),
                    null);
            return;
        }

        // refund link
        String refundId = internalRefundId.trim();
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("UNKNOWN_REFUND"));

        line.setInternalRefundId(refund.getId());
        line.setInternalPaymentTransactionId(null);
        payoutLineRepository.save(line);

        auditService.logAction(
                "PAYOUT_LINE_LINKED",
                "PayoutLine",
                payoutLineId,
                performedBy,
                "payoutId=" + payoutId + ", linkedRefundId=" + refund.getId() + (trimmedNotes != null ? ", notes=" + trimmedNotes : ""),
                null);
    }

    @Transactional
    public void unlinkPayoutLine(
            String payoutId,
            String payoutLineId,
            String performedBy,
            String notes) {
        PayoutLine line = payoutLineRepository.findById(payoutLineId)
                .orElseThrow(() -> new IllegalArgumentException("UNKNOWN_PAYOUT_LINE"));

        if (!payoutId.equals(line.getPayoutId())) {
            throw new IllegalArgumentException("LINE_NOT_IN_PAYOUT");
        }

        line.setInternalPaymentTransactionId(null);
        line.setInternalRefundId(null);
        payoutLineRepository.save(line);

        String trimmedNotes = (notes != null && !notes.isBlank()) ? notes.trim() : null;
        auditService.logAction(
                "PAYOUT_LINE_UNLINKED",
                "PayoutLine",
                payoutLineId,
                performedBy,
                "payoutId=" + payoutId + (trimmedNotes != null ? ", notes=" + trimmedNotes : ""),
                null);
    }

    public List<AdminPayoutDto> getPayouts(String gatewayType) {
        List<Payout> payouts;
        if (gatewayType == null || gatewayType.isBlank()) {
            payouts = payoutRepository.findAll(Sort.by(Sort.Direction.DESC, "payoutAt"));
        } else {
            payouts = payoutRepository.findByGatewayTypeOrderByPayoutAtDesc(gatewayType);
        }

        return payouts.stream()
                .map(p -> {
                    long totalLines = payoutLineRepository.countByPayoutId(p.getId());
                    long matchedLines = payoutLineRepository.countMatchedByPayoutId(p.getId());
                    long issuesCount = Math.max(0, totalLines - matchedLines);
                    BigDecimal matchedPercent = totalLines > 0
                            ? BigDecimal.valueOf(matchedLines)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(BigDecimal.valueOf(totalLines), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return AdminPayoutDto.builder()
                            .payoutId(p.getId())
                            .gatewayType(p.getGatewayType())
                            .providerPayoutId(p.getProviderPayoutId())
                            .status(p.getStatus())
                            .payoutAt(p.getPayoutAt())
                            .currency(p.getCurrency())
                            .grossAmount(p.getGrossAmount())
                            .feeAmount(p.getFeeAmount())
                            .netAmount(p.getNetAmount())
                            .totalLines(totalLines)
                            .matchedLines(matchedLines)
                            .issuesCount(issuesCount)
                            .matchedPercent(matchedPercent)
                            .build();
                })
                .toList();
    }

    public AdminPayoutDetailDto getPayoutDetail(String payoutId) {
        Payout payout = payoutRepository.findById(payoutId)
                .orElseThrow(() -> new RuntimeException("Payout not found: " + payoutId));

        long totalLines = payoutLineRepository.countByPayoutId(payoutId);
        long matchedLines = payoutLineRepository.countMatchedByPayoutId(payoutId);
        long issuesCount = Math.max(0, totalLines - matchedLines);
        BigDecimal matchedPercent = totalLines > 0
                ? BigDecimal.valueOf(matchedLines)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalLines), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<PayoutLine> lines = payoutLineRepository.findByPayoutId(payoutId);

        List<AdminPayoutDetailDto.PayoutLineDto> lineDtos = lines.stream()
                .map(l -> AdminPayoutDetailDto.PayoutLineDto.builder()
                        .id(l.getId())
                        .lineType(l.getLineType())
                        .providerBalanceTxnId(l.getProviderBalanceTxnId())
                        .internalPaymentTransactionId(l.getInternalPaymentTransactionId())
                        .internalRefundId(l.getInternalRefundId())
                        .grossAmount(l.getGrossAmount())
                        .feeAmount(l.getFeeAmount())
                        .netAmount(l.getNetAmount())
                        .currency(l.getCurrency())
                        .occurredAt(l.getOccurredAt())
                        .build())
                .toList();

        return AdminPayoutDetailDto.builder()
                .payoutId(payout.getId())
                .gatewayType(payout.getGatewayType())
                .providerPayoutId(payout.getProviderPayoutId())
                .status(payout.getStatus())
                .payoutAt(payout.getPayoutAt())
                .currency(payout.getCurrency())
                .grossAmount(payout.getGrossAmount())
                .feeAmount(payout.getFeeAmount())
                .netAmount(payout.getNetAmount())
                .createdAt(payout.getCreatedAt())
                .totalLines(totalLines)
                .matchedLines(matchedLines)
                .issuesCount(issuesCount)
                .matchedPercent(matchedPercent)
                .lines(lineDtos)
                .build();
    }

    // ── Dashboard Analytics (All From Local DB) ───────────────────

    public long getTotalTransactionCount() {
        return transactionRepository.count();
    }

    public long getTodayTransactionCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return transactionRepository.findSucceededInRange(startOfDay, endOfDay).size();
    }

    public long getFailedTransactionCount() {
        return transactionRepository.findByStatus("FAILED").size();
    }

    public long getPendingRefundCount() {
        // Placeholder — AdminRefundService should be used for this
        return 0;
    }

    // ── Gateway Performance Metrics (Local Perspective) ───────────

    public List<AdminGatewayStatusDto> getGatewayStatuses() {
        List<PaymentTransaction> allTxs = transactionRepository.findAll();

        Map<String, List<PaymentTransaction>> byGateway = new HashMap<>();
        byGateway.put("stripe", new ArrayList<>());
        byGateway.put("razorpay", new ArrayList<>());

        for (PaymentTransaction tx : allTxs) {
            String gw = resolveGatewayType(tx);
            byGateway.computeIfAbsent(gw, k -> new ArrayList<>()).add(tx);
        }

        List<AdminGatewayStatusDto> statuses = new ArrayList<>();
        for (Map.Entry<String, List<PaymentTransaction>> entry : byGateway.entrySet()) {
            statuses.add(buildGatewayStatus(entry.getKey(), entry.getValue()));
        }

        return statuses;
    }

    // ── Private Helpers ───────────────────────────────────────────

    private AdminGatewayStatusDto buildGatewayStatus(String gatewayName, List<PaymentTransaction> txs) {
        long total = txs.size();
        long failed = txs.stream().filter(t -> "FAILED".equalsIgnoreCase(t.getStatus())).count();
        double failureRate = total > 0 ? (double) failed / total * 100.0 : 0.0;

        BigDecimal totalAmount = txs.stream()
                .filter(t -> "SUCCEEDED".equalsIgnoreCase(t.getStatus()))
                .map(PaymentTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AdminGatewayStatusDto.builder()
                .gateway(gatewayName)
                .healthy(failureRate < 10.0)
                .totalTransactions(total)
                .todayTransactions(countToday(txs))
                .failureRate(BigDecimal.valueOf(failureRate).setScale(2, RoundingMode.HALF_UP).doubleValue())
                .totalAmount(totalAmount)
                .uptime(failureRate < 10.0 ? 99.9 : 95.0)
                .lastChecked(LocalDateTime.now())
                .message(failureRate < 10.0 ? "Operational" : "Elevated failure rate detected")
                .build();
    }

    private long countToday(List<PaymentTransaction> txs) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return txs.stream().filter(t -> t.getCreatedAt() != null && t.getCreatedAt().isAfter(startOfDay)).count();
    }

    private String resolveGatewayType(PaymentTransaction tx) {
        String txId = tx.getGatewayTransactionId();
        if (txId != null && txId.startsWith("pi_")) {
            return "stripe";
        } else if (txId != null && txId.startsWith("pay_")) {
            return "razorpay";
        }
        return "stripe"; // default
    }

    private AdminPaymentTransactionDto convertToDto(PaymentTransaction transaction) {
        return AdminPaymentTransactionDto.builder()
                .paymentId(transaction.getId())
                .orderId(transaction.getOrderId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .gatewayType(resolveGatewayType(transaction))
                .gatewayTransactionId(transaction.getGatewayTransactionId())
                .paymentMethod(transaction.getPaymentMethodType())
                .createdAt(transaction.getCreatedAt())
                .failureReason(transaction.getErrorMessage())
                .build();
    }
}
