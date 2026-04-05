package com.homebase.ecom.payment.service.impl;

import com.homebase.ecom.payment.infrastructure.persistence.entity.WebhookEvent;
import com.homebase.ecom.payment.infrastructure.persistence.repository.WebhookEventRepository;
import com.homebase.ecom.payment.gateway.GatewayEvent;
import com.homebase.ecom.payment.gateway.WebhookProcessor;
import com.homebase.ecom.payment.gateway.WebhookProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class WebhookServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(WebhookServiceImpl.class);

    private final WebhookProcessorFactory processorFactory;
    private final WebhookEventRepository webhookEventRepository;
    private final PaymentServiceImpl paymentService;

    public WebhookServiceImpl(WebhookProcessorFactory processorFactory,
            WebhookEventRepository webhookEventRepository,
            PaymentServiceImpl paymentService) {
        this.processorFactory = processorFactory;
        this.webhookEventRepository = webhookEventRepository;
        this.paymentService = paymentService;
    }

    public void processIncomingWebhook(String gatewayType, String payload, String signature) {
        // DO NOT log full payload - may contain PII and sensitive data
        log.info("Incoming webhook from gateway: {}", gatewayType);

        // Step 1: Verify signature OUTSIDE transaction (can fail fast)
        WebhookProcessor processor = processorFactory.getProcessor(gatewayType)
                .orElseThrow(() -> new IllegalArgumentException("No webhook processor for: " + gatewayType));

        GatewayEvent gatewayEvent = processor.process(payload, signature);
        log.info("Webhook event verified: {} (type: {})", gatewayEvent.getEventId(), gatewayEvent.getEventType());

        // Step 2: Persist first (append-only)
        WebhookEvent eventEntity = getOrCreateWebhookEvent(gatewayEvent, payload);

        if (eventEntity.isProcessed()) {
            log.info("Duplicate webhook ignored (already processed): {}", gatewayEvent.getEventId());
            return;
        }

        // Step 3: Process business logic in its own transaction.
        // If it fails, we still keep the webhook record and store the failure state.
        try {
            processGatewayEventInTransaction(gatewayEvent);
            markWebhookProcessed(eventEntity.getId());
            log.info("Webhook event processed successfully: {}", gatewayEvent.getEventId());
        } catch (Exception e) {
            recordWebhookFailure(eventEntity.getId(), e);
            log.error("Failed to process webhook event {}: {}", gatewayEvent.getEventId(), e.getMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected WebhookEvent getOrCreateWebhookEvent(GatewayEvent gatewayEvent, String payload) {
        return webhookEventRepository.findByGatewayEventId(gatewayEvent.getEventId())
                .orElseGet(() -> {
                    WebhookEvent entity = new WebhookEvent();
                    entity.setGatewayEventId(gatewayEvent.getEventId());
                    entity.setEventType(gatewayEvent.getEventType());
                    entity.setPayload(payload);
                    entity.setProcessed(false);

                    try {
                        return webhookEventRepository.save(entity);
                    } catch (DataIntegrityViolationException dup) {
                        // Race: another thread inserted the same gateway_event_id.
                        return webhookEventRepository.findByGatewayEventId(gatewayEvent.getEventId())
                                .orElseThrow(() -> dup);
                    }
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void processGatewayEventInTransaction(GatewayEvent gatewayEvent) {
        if ("PAYMENT_SUCCESS".equals(gatewayEvent.getEventType())) {
            paymentService.handlePaymentSuccess(
                    gatewayEvent.getOrderId(),
                    gatewayEvent.getGatewayTransactionId(),
                    gatewayEvent.getEventId(),
                    gatewayEvent.getAmount());
        } else if ("PAYMENT_EXPIRED".equals(gatewayEvent.getEventType())) {
            paymentService.handlePaymentExpired(
                    gatewayEvent.getOrderId(),
                    gatewayEvent.getGatewayTransactionId());
        } else if ("PAYMENT_FAILED".equals(gatewayEvent.getEventType())) {
            paymentService.handlePaymentFailed(
                    gatewayEvent.getOrderId(),
                    gatewayEvent.getGatewayTransactionId(),
                    "gateway_reported_failure");
        } else if ("REFUND_SUCCESS".equals(gatewayEvent.getEventType())) {
            paymentService.handleRefundSuccess(
                    gatewayEvent.getGatewayTransactionId(),
                    gatewayEvent.getAmount(),
                    null);
        }
        // else: ignore unknown events (still persisted + can be audited)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void markWebhookProcessed(String webhookEventId) {
        WebhookEvent event = webhookEventRepository.findById(webhookEventId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown webhookEventId: " + webhookEventId));

        event.setProcessed(true);
        event.setProcessedAt(LocalDateTime.now());
        event.setErrorMessage(null);
        webhookEventRepository.save(event);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void recordWebhookFailure(String webhookEventId, Exception e) {
        WebhookEvent event = webhookEventRepository.findById(webhookEventId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown webhookEventId: " + webhookEventId));

        event.setProcessed(false);
        event.setErrorMessage(e.getMessage());
        webhookEventRepository.save(event);
    }
}
