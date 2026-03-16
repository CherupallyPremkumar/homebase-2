package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.payment.gateway.service.PaymentService;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.dto.TransitionContext;
import org.chenile.workflow.param.MinimalPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * OWIZ saga step 7: Initiate payment.
 * Uses payment-client's PaymentService (StateEntityService) to create a payment entity.
 * Sets checkout.paymentId upon initiation.
 */
public class InitiatePaymentCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(InitiatePaymentCommand.class);

    @Autowired(required = false)
    private PaymentService paymentServiceClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (paymentServiceClient != null) {
            MinimalPayload payload = new MinimalPayload();
            payload.setComment("Payment for checkout " + checkout.getId());
            payload.setAttribute("checkoutId", checkout.getId());
            payload.setAttribute("orderId", checkout.getOrderId());
            payload.setAttribute("amount", checkout.getTotal());
            payload.setAttribute("customerId", checkout.getCustomerId());
            payload.setAttribute("paymentMethodId", checkout.getPaymentMethodId());

            StateEntityServiceResponse<Object> response = paymentServiceClient.processById(null, "create", payload);
            if (response != null && response.getMutatedEntity() != null) {
                // Payment entity is generic Object — extract ID via reflection or toString
                Object payment = response.getMutatedEntity();
                // PaymentService uses StateEntityService<Object>, entity should have getId()
                try {
                    String paymentId = (String) payment.getClass().getMethod("getId").invoke(payment);
                    checkout.setPaymentId(paymentId);
                } catch (Exception e) {
                    log.warn("[CHECKOUT SAGA] Could not extract paymentId: {}", e.getMessage());
                }
            }

            log.info("[CHECKOUT SAGA] Payment initiated for checkout {}, orderId={}, paymentId={}",
                    checkout.getId(), checkout.getOrderId(), checkout.getPaymentId());
        }
        checkout.setLastCompletedStep("initiatePayment");
    }
}
