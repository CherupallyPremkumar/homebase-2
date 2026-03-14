package com.homebase.ecom.returnrequest.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes return request domain events via Spring's ApplicationEventPublisher.
 * In production, these events can be bridged to Kafka or other messaging systems.
 */
@Component
public class ReturnRequestEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public ReturnRequestEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishReturnApproved(ReturnApprovedEvent event) {
        log.info("Publishing ReturnApprovedEvent for return request: {}, orderId: {}",
                event.getReturnRequestId(), event.getOrderId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReturnReceived(ReturnReceivedEvent event) {
        log.info("Publishing ReturnReceivedEvent for return request: {}, warehouseId: {}",
                event.getReturnRequestId(), event.getWarehouseId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishRefundProcessed(RefundProcessedEvent event) {
        log.info("Publishing RefundProcessedEvent for return request: {}, amount: {}",
                event.getReturnRequestId(), event.getRefundAmount());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReturnRejected(ReturnRejectedEvent event) {
        log.info("Publishing ReturnRejectedEvent for return request: {}, reason: {}",
                event.getReturnRequestId(), event.getRejectionReason());
        applicationEventPublisher.publishEvent(event);
    }
}
