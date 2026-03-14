package com.homebase.ecom.checkout.service.saga.steps;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.OrderDetails;
import com.homebase.ecom.checkout.domain.saga.RetryPolicy;
import com.homebase.ecom.checkout.domain.saga.SagaExecutionContext;
import com.homebase.ecom.checkout.domain.saga.SagaStep;
import com.homebase.ecom.checkout.domain.saga.SagaStepResult;
import com.homebase.ecom.checkout.service.port.OrderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateOrderStep implements SagaStep {
    private static final Logger log = LoggerFactory.getLogger(CreateOrderStep.class);
    private final OrderClient orderClient;
    private final RetryPolicy retryPolicy = RetryPolicy.builder().maxRetries(3).initialDelayMs(1000).build();

    public CreateOrderStep(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Override
    public SagaStepResult execute(Checkout checkout, SagaExecutionContext context) {
        log.info("Executing CreateOrderStep for checkout: {}", checkout.getCheckoutId());
        try {
            OrderDetails details = orderClient.createOrder(checkout);
            checkout.setOrderId(details.getOrderId());
            return SagaStepResult.builder().stepName(getStepName()).success(true).data(details).build();
        } catch (Exception e) {
            log.error("Failed to create order", e);
            return SagaStepResult.builder().stepName(getStepName()).success(false).errorMessage(e.getMessage()).build();
        }
    }

    @Override
    public void compensate(Checkout checkout, SagaExecutionContext context) {
        log.info("Compensating CreateOrderStep for order: {}", checkout.getOrderId());
        if (checkout.getOrderId() != null) {
            try {
                orderClient.cancelOrder(checkout.getOrderId().toString());
            } catch (Exception e) {
                log.error("Failed to cancel order during compensation", e);
            }
        }
    }

    @Override
    public String getStepName() { return "CreateOrderStep"; }
    @Override
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
}
