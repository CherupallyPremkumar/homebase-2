package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.PaymentInitiationPort;
import com.homebase.ecom.checkout.domain.port.PaymentInitiationPort.PaymentResult;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 7: Initiate payment.
 * Uses PaymentInitiationPort (hexagonal) — adapter calls Payment service
 * which internally creates a Razorpay order.
 */
public class InitiatePaymentCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(InitiatePaymentCommand.class);

    private final PaymentInitiationPort paymentInitiationPort;

    public InitiatePaymentCommand(PaymentInitiationPort paymentInitiationPort) {
        this.paymentInitiationPort = paymentInitiationPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        PaymentResult result = paymentInitiationPort.initiatePayment(
                checkout.getId(),
                checkout.getOrderId(),
                checkout.getTotal(),
                checkout.getCustomerId(),
                checkout.getPaymentMethodId()
        );

        checkout.setPaymentId(result.paymentId());

        checkout.setLastCompletedStep("initiatePayment");
        log.info("[CHECKOUT SAGA] Payment initiated for checkout {}, paymentId={}, gatewayOrderId={}",
                checkout.getId(), result.paymentId(), result.gatewayOrderId());
    }
}
