package com.homebase.ecom.checkout.service.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.checkout.domain.model.*;
import com.homebase.ecom.checkout.domain.saga.*;
import com.homebase.ecom.checkout.domain.repository.*;
import com.homebase.ecom.checkout.service.port.*;
import com.homebase.ecom.checkout.service.saga.steps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class CheckoutSagaOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(CheckoutSagaOrchestrator.class);

    private final CartClient cartClient;
    private final PricingClient pricingClient;
    private final OrderClient orderClient;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final ShippingClient shippingClient;
    private final PromoClient promoClient;

    private final CheckoutRepository checkoutRepository;
    private final SagaExecutionLogRepository sagaLogRepository;
    private final ObjectMapper objectMapper;

    public CheckoutSagaOrchestrator(CartClient cartClient, PricingClient pricingClient,
            OrderClient orderClient, PaymentClient paymentClient,
            InventoryClient inventoryClient, ShippingClient shippingClient,
            PromoClient promoClient, CheckoutRepository checkoutRepository,
            SagaExecutionLogRepository sagaLogRepository, ObjectMapper objectMapper) {
        this.cartClient = cartClient;
        this.pricingClient = pricingClient;
        this.orderClient = orderClient;
        this.paymentClient = paymentClient;
        this.inventoryClient = inventoryClient;
        this.shippingClient = shippingClient;
        this.promoClient = promoClient;
        this.checkoutRepository = checkoutRepository;
        this.sagaLogRepository = sagaLogRepository;
        this.objectMapper = objectMapper;
    }

    public Checkout executeCheckout(UUID cartId, UUID userId, String couponCode, String idempotencyKey) {
        log.info("Starting modernized checkout saga for user: {} and cart: {}", userId, cartId);

        // Idempotency check
        Optional<Checkout> existing = checkoutRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            log.info("Duplicate checkout request with key: {}", idempotencyKey);
            return existing.get();
        }

        Checkout checkout = new Checkout(cartId, userId, idempotencyKey);

        List<SagaStep> steps = Arrays.asList(
            new LockCartStep(cartClient),
            new LockPriceStep(pricingClient),
            new ValidateInventoryStep(inventoryClient),
            new ValidateShippingStep(shippingClient),
            new CreateOrderStep(orderClient),
            new CommitPromoStep(promoClient),
            new InitiatePaymentStep(paymentClient)
        );

        SagaExecutionContext context = new SagaExecutionContext(
            UUID.randomUUID().toString(),
            checkout,
            new HashMap<>(Map.of("couponCode", couponCode != null ? couponCode : "")),
            new ArrayList<>()
        );

        try {
            for (int i = 0; i < steps.size(); i++) {
                SagaStep step = steps.get(i);
                SagaStepResult result = executeStepWithRetry(step, checkout, context);

                logExecution(checkout.getCheckoutId(), step.getStepName(), "COMPLETED", result);

                if (!result.isSuccess()) {
                    handleFailure(checkout, steps, i, result.getErrorMessage(), context);
                    throw new RuntimeException("Checkout failed at step: " + step.getStepName() + ". Error: " + result.getErrorMessage());
                }
            }

            checkoutRepository.save(checkout);
            return checkout;
        } catch (Exception e) {
            log.error("Checkout saga failed", e);
            throw e;
        }
    }

    private SagaStepResult executeStepWithRetry(SagaStep step, Checkout checkout, SagaExecutionContext context) {
        RetryPolicy policy = step.getRetryPolicy();
        Exception lastException = null;

        for (int attempt = 0; attempt <= policy.getMaxRetries(); attempt++) {
            try {
                return step.execute(checkout, context);
            } catch (Exception e) {
                lastException = e;
                if (attempt < policy.getMaxRetries()) {
                    try {
                        long delay = (long) (policy.getInitialDelayMs() * Math.pow(policy.getBackoffMultiplier(), attempt));
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        return SagaStepResult.builder()
            .stepName(step.getStepName())
            .success(false)
            .errorMessage(lastException != null ? lastException.getMessage() : "Unknown failure")
            .build();
    }

    private void handleFailure(Checkout checkout, List<SagaStep> steps, int failedStepIndex, String error, SagaExecutionContext context) {
        log.error("Handling saga failure at step {}: {}", failedStepIndex, error);
        checkout.transitionTo(CheckoutState.PAYMENT_FAILED);

        for (int i = failedStepIndex - 1; i >= 0; i--) {
            try {
                steps.get(i).compensate(checkout, context);
                logExecution(checkout.getCheckoutId(), steps.get(i).getStepName(), "COMPENSATED", null);
            } catch (Exception e) {
                log.error("Compensation failed for step: {}", steps.get(i).getStepName(), e);
                logExecution(checkout.getCheckoutId(), steps.get(i).getStepName(), "COMPENSATION_FAILED", null);
            }
        }
        checkoutRepository.save(checkout);
    }

    private void logExecution(UUID checkoutId, String stepName, String status, SagaStepResult result) {
        String errorMsg = null;
        String data = null;
        if (result != null) {
            errorMsg = result.getErrorMessage();
            try {
                data = objectMapper.writeValueAsString(result.getData());
            } catch (JsonProcessingException e) {
                data = "Serialization failed";
            }
        }
        sagaLogRepository.log(checkoutId, stepName, status, errorMsg, data);
    }
}
