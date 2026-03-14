package com.homebase.ecom.payment.service.impl;

import com.homebase.ecom.payment.domain.PaymentStatus;
import com.homebase.ecom.payment.domain.PaymentTransaction;
import com.homebase.ecom.payment.domain.RefundRequestStatus;
import com.homebase.ecom.payment.dto.CheckoutSessionRequest;
import com.homebase.ecom.payment.dto.CheckoutSessionResponse;
import com.homebase.ecom.payment.gateway.PaymentGatewayFactory;
import com.homebase.ecom.payment.repository.PaymentTransactionRepository;
import com.homebase.ecom.payment.repository.RefundRequestRepository;
import com.homebase.ecom.payment.ledger.service.LedgerService;
import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.PaymentFailedEvent;
import com.homebase.ecom.shared.event.PaymentRefundedEvent;
import com.homebase.ecom.shared.event.PaymentSucceededEvent;
import com.homebase.ecom.shared.CurrencyResolver;
import com.homebase.ecom.shared.event.PaymentSessionExpiredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.chenile.cconfig.sdk.CconfigClient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentGatewayFactory gatewayFactory;
    private final DynamicGatewayResolver gatewayResolver;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RefundRequestRepository refundRequestRepository;
    private final LedgerService ledgerService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CconfigClient cconfigClient;
    private final CurrencyResolver currencyResolver;

    public PaymentServiceImpl(PaymentGatewayFactory gatewayFactory,
            DynamicGatewayResolver gatewayResolver,
            PaymentTransactionRepository paymentTransactionRepository,
            RefundRequestRepository refundRequestRepository,
            LedgerService ledgerService,
            KafkaTemplate<String, Object> kafkaTemplate,
            CconfigClient cconfigClient,
            CurrencyResolver currencyResolver) {
        this.gatewayFactory = gatewayFactory;
        this.gatewayResolver = gatewayResolver;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.ledgerService = ledgerService;
        this.kafkaTemplate = kafkaTemplate;
        this.cconfigClient = cconfigClient;
        this.currencyResolver = currencyResolver;
    }

    @Transactional
    public CheckoutSessionResponse initiateCheckout(CheckoutSessionRequest request) {
        String activeGatewayType = gatewayResolver.getActiveGatewayType();
        log.info("Initiating checkout for order: {} using gateway: {}",
                request.getOrderId(), activeGatewayType);

        CheckoutSessionResponse response = gatewayFactory.getGatewayByType(activeGatewayType)
                .createCheckoutSession(request);

        // Note: Order module will link session ID via OrderService
        // Payment module should not directly modify Order entity
        log.info("Checkout session created: {} for order: {}",
                response.getSessionId(), request.getOrderId());

        return response;
    }

    @Transactional
    public void processRefund(String orderId, String gatewayTransactionId, BigDecimal amount, String reason) {
        log.info("Processing refund for order: {} (Amount: {})", orderId, amount);

        if (gatewayTransactionId == null || gatewayTransactionId.isEmpty()) {
            throw new RuntimeException("Cannot refund order without gateway transaction: " + orderId);
        }

        // Get payment transaction to determine which gateway to use
        PaymentTransaction tx = paymentTransactionRepository
                .findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found: " + gatewayTransactionId));

        // Use the gateway that processed the original payment
        String gatewayType = tx.getGatewayType() != null ? tx.getGatewayType() : gatewayResolver.getActiveGatewayType();
        gatewayFactory.getGatewayByType(gatewayType).processRefund(gatewayTransactionId, amount, reason);

        log.info("Refund request sent to {} gateway for order: {}", gatewayType, orderId);
        // Note: Order status will be updated via PaymentRefundedEvent → OrderSaga
    }

    @Transactional
    public void handlePaymentSuccess(String orderId, String gatewayTransactionId, String gatewayEventId,
            BigDecimal amount) {
        log.info("Handling payment success for order: {} (TX: {})", orderId, gatewayTransactionId);

        // Check if payment transaction already exists (idempotency)
        PaymentTransaction existingTx = paymentTransactionRepository
                .findByGatewayTransactionId(gatewayTransactionId)
                .orElse(null);

        if (existingTx != null && existingTx.getStatusEnum() == PaymentStatus.SUCCEEDED) {
            log.info("Payment transaction {} already processed as SUCCEEDED. Skipping.", gatewayTransactionId);
            return;
        }

        // Save payment transaction record
        PaymentTransaction tx = existingTx != null ? existingTx : new PaymentTransaction();
        tx.setOrderId(orderId);
        tx.setGatewayType(gatewayResolver.getActiveGatewayType());
        tx.setGatewayTransactionId(gatewayTransactionId);
        tx.setGatewayEventId(gatewayEventId);
        tx.setStatus(PaymentStatus.SUCCEEDED);
        tx.setAmount(new Money(amount, getDefaultCurrency()));
        paymentTransactionRepository.save(tx);

        // Ledger integration (idempotent by transactionId)
        if (amount != null) {
            ledgerService.recordChargeSuccess(gatewayTransactionId, amount, BigDecimal.ZERO);
        }

        // Publish event - OrderSaga will update order status (after DB commit)
        PaymentSucceededEvent event = new PaymentSucceededEvent(orderId, gatewayTransactionId, LocalDateTime.now());
        if (amount != null) {
            event.setAmount(new Money(amount, getDefaultCurrency()));
        }
        sendAfterCommit(KafkaTopics.PAYMENT_EVENTS, orderId,
                EventEnvelope.of(PaymentSucceededEvent.EVENT_TYPE, 1, event));

        log.info("Payment success event scheduled for publish (after commit) for order: {}.", orderId);
    }

    @Transactional
    public void handlePaymentExpired(String orderId, String gatewaySessionId) {
        log.info("Handling payment expiry for order: {} (session: {})", orderId, gatewaySessionId);
        PaymentSessionExpiredEvent event = new PaymentSessionExpiredEvent(orderId, LocalDateTime.now());
        sendAfterCommit(KafkaTopics.PAYMENT_EVENTS, orderId,
                EventEnvelope.of(PaymentSessionExpiredEvent.EVENT_TYPE, 1, event));
    }

    @Transactional
    public void handlePaymentFailed(String orderId, String gatewayTransactionId, String reason) {
        log.info("Handling payment failure for order: {} (tx: {})", orderId, gatewayTransactionId);
        PaymentFailedEvent event = new PaymentFailedEvent(orderId, gatewayTransactionId, reason, LocalDateTime.now());
        sendAfterCommit(KafkaTopics.PAYMENT_EVENTS, orderId,
                EventEnvelope.of(PaymentFailedEvent.EVENT_TYPE, 1, event));
    }

    @Transactional
    public void handleRefundSuccess(String gatewayTransactionId, BigDecimal refundedAmount, String refundReason) {
        log.info("Handling refund success for tx: {}", gatewayTransactionId);

        PaymentTransaction tx = paymentTransactionRepository
                .findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found: " + gatewayTransactionId));

        tx.setStatus(PaymentStatus.REFUNDED);
        paymentTransactionRepository.save(tx);

        // Mark any local refund requests as completed (best-effort)
        try {
            var requests = refundRequestRepository.findByOrderId(tx.getOrderId());
            for (var req : requests) {
                var status = req.getStatusEnum();
                if (status == RefundRequestStatus.INITIATED || status == RefundRequestStatus.APPROVED) {
                    req.setStatus(RefundRequestStatus.PROCESSED);
                }
            }
            refundRequestRepository.saveAll(requests);
        } catch (Exception e) {
            log.warn("Failed to update refund request status for order {}: {}", tx.getOrderId(), e.getMessage());
        }

        // Ledger integration (idempotent by transactionId)
        if (refundedAmount != null) {
            ledgerService.recordRefund(gatewayTransactionId, refundedAmount);
        }

        PaymentRefundedEvent event = new PaymentRefundedEvent(
                tx.getOrderId(),
                gatewayTransactionId,
                refundedAmount,
                refundReason,
                LocalDateTime.now());
        if (refundedAmount != null) {
            event.setRefundedMoney(new Money(refundedAmount, getDefaultCurrency()));
        }

        sendAfterCommit(KafkaTopics.PAYMENT_EVENTS, tx.getOrderId(),
                EventEnvelope.of(PaymentRefundedEvent.EVENT_TYPE, 1, event));
    }

    private String getDefaultCurrency() {
        return currencyResolver.resolve().code();
    }

    private void sendAfterCommit(String topic, String key, Object payload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaTemplate.send(topic, key, payload);
                }
            });
        } else {
            kafkaTemplate.send(topic, key, payload);
        }
    }
}
