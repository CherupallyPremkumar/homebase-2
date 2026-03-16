package com.homebase.ecom.returnrequest.service.event;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes return request domain events via Spring's ApplicationEventPublisher.
 * In production, these events are bridged to Kafka return.events topic.
 *
 * Publishes: RETURN_REQUESTED, RETURN_APPROVED, RETURN_REJECTED, RETURN_COMPLETED
 */
@Component
public class ReturnRequestEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public ReturnRequestEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishReturnRequested(Returnrequest returnrequest) {
        ReturnRequestedEvent event = new ReturnRequestedEvent(
                returnrequest.getId(), returnrequest.orderId, returnrequest.customerId,
                returnrequest.reason, returnrequest.returnType);
        log.info("Publishing RETURN_REQUESTED for return request: {}, orderId: {}",
                event.getReturnRequestId(), event.getOrderId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReturnApproved(Returnrequest returnrequest) {
        ReturnApprovedEvent event = new ReturnApprovedEvent(
                returnrequest.getId(), returnrequest.orderId, returnrequest.customerId,
                returnrequest.totalRefundAmount, returnrequest.returnType);
        log.info("Publishing RETURN_APPROVED for return request: {}, orderId: {}",
                event.getReturnRequestId(), event.getOrderId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReturnRejected(Returnrequest returnrequest) {
        ReturnRejectedEvent event = new ReturnRejectedEvent(
                returnrequest.getId(), returnrequest.orderId, returnrequest.customerId,
                returnrequest.rejectionReason, returnrequest.rejectionComment);
        log.info("Publishing RETURN_REJECTED for return request: {}, orderId: {}",
                event.getReturnRequestId(), event.getOrderId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReturnCompleted(Returnrequest returnrequest) {
        ReturnCompletedEvent event = new ReturnCompletedEvent(
                returnrequest.getId(), returnrequest.orderId, returnrequest.customerId,
                returnrequest.totalRefundAmount, returnrequest.returnType);
        log.info("Publishing RETURN_COMPLETED for return request: {}, orderId: {}",
                event.getReturnRequestId(), event.getOrderId());
        applicationEventPublisher.publishEvent(event);
    }
}
