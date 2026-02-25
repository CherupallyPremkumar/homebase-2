package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminRefundDto;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.domain.RefundRequest;
import com.ecommerce.payment.repository.RefundRequestRepository;
import com.ecommerce.payment.service.impl.PaymentServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminRefundService {

    private final RefundRequestRepository refundRepository;
    private final OrderService orderService;
    private final PaymentServiceImpl paymentService;
    private final AdminAuditService auditService;

    public AdminRefundService(RefundRequestRepository refundRepository,
            OrderService orderService,
            PaymentServiceImpl paymentService,
            AdminAuditService auditService) {
        this.refundRepository = refundRepository;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.auditService = auditService;
    }

    public Page<AdminRefundDto> getAllRefunds(Pageable pageable) {
        return refundRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public AdminRefundDto approveRefund(String refundId, String adminUser) {
        RefundRequest refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found: " + refundId));

        if (!"PENDING".equalsIgnoreCase(refund.getStatus())) {
            throw new RuntimeException("Refund is not in PENDING status");
        }

        refund.setStatus("APPROVED");

        RefundRequest saved = refundRepository.save(refund);

        auditService.logAction("REFUND_APPROVE", "RefundRequest", refundId, adminUser, "Approved refund request");

        return convertToDto(saved);
    }

    @Transactional
    public AdminRefundDto processRefund(String refundId, String adminUser) {
        RefundRequest refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found: " + refundId));

        if (!"APPROVED".equalsIgnoreCase(refund.getStatus())) {
            throw new RuntimeException("Refund must be APPROVED before processing");
        }

        // Initiate refund with the same gateway used for the payment
        var order = orderService.getOrder(refund.getOrderId());
        paymentService.processRefund(refund.getOrderId(), order.getGatewayTransactionId(), refund.getAmount(), refund.getReason());

        refund.setStatus("INITIATED");
        RefundRequest saved = refundRepository.save(refund);

        auditService.logAction("REFUND_INITIATED", "RefundRequest", refundId, adminUser, "Initiated refund via gateway");

        return convertToDto(saved);
    }

    private AdminRefundDto convertToDto(RefundRequest refund) {
        String status = refund.getStatus();
        String uiStatus = switch (status != null ? status.toUpperCase() : "") {
            case "PROCESSED" -> "COMPLETED";
            case "INITIATED" -> "PROCESSING";
            default -> status;
        };

        return AdminRefundDto.builder()
                .refundId(refund.getId())
                .paymentId(refund.getOrderId()) // Using orderId as payment link for now
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(uiStatus)
                .gatewayRefundId(refund.getGatewayRefundId())
                .createdAt(refund.getCreatedAt())
                .build();
    }
}
