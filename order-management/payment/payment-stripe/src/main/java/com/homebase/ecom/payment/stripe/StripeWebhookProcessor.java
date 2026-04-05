package com.homebase.ecom.payment.stripe;

import com.homebase.ecom.payment.exception.WebhookSignatureException;
import com.homebase.ecom.payment.gateway.GatewayEvent;
import com.homebase.ecom.payment.gateway.WebhookProcessor;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StripeWebhookProcessor implements WebhookProcessor {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookProcessor.class);

    private final StripeGateway stripeGateway;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookProcessor(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public boolean supports(String gatewayType) {
        return "STRIPE".equalsIgnoreCase(gatewayType);
    }

    @Override
    public GatewayEvent process(String payload, String signature) {
        try {
            Event event = stripeGateway.constructWebhookEvent(payload, signature, webhookSecret);
            log.info("Processing Stripe webhook: {} ({})", event.getType(), event.getId());

            GatewayEvent gatewayEvent = new GatewayEvent();
            gatewayEvent.setEventId(event.getId());

            switch (event.getType()) {
                case "checkout.session.completed":
                    Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                    gatewayEvent.setEventType("PAYMENT_SUCCESS");
                    gatewayEvent.setOrderId(session.getMetadata().get("order_id"));
                    gatewayEvent.setGatewayTransactionId(session.getPaymentIntent());

                    // IMPORTANT: PaymentServiceImpl persists PaymentTransaction.amount (NOT NULL).
                    // Stripe session amounts are in the smallest currency unit (paise for INR).
                    if (session.getAmountTotal() != null) {
                        gatewayEvent.setAmount(BigDecimal.valueOf(session.getAmountTotal())
                                .movePointLeft(2)
                                .setScale(2, RoundingMode.HALF_UP));
                    }
                    if (session.getCurrency() != null) {
                        gatewayEvent.setCurrency(session.getCurrency().toUpperCase());
                    }
                    break;
                case "checkout.session.expired":
                    Session expiredSession = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                    gatewayEvent.setEventType("PAYMENT_EXPIRED");
                    gatewayEvent.setOrderId(expiredSession.getMetadata().get("order_id"));
                    if (expiredSession.getCurrency() != null) {
                        gatewayEvent.setCurrency(expiredSession.getCurrency().toUpperCase());
                    }
                    break;
                case "charge.refunded":
                    Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElseThrow();
                    gatewayEvent.setEventType("REFUND_SUCCESS");
                    gatewayEvent.setGatewayTransactionId(charge.getPaymentIntent());
                    // Stripe amounts are in the smallest currency unit (paise for INR)
                    // Internally we store amounts as NUMERIC(10,2) in rupees.
                    gatewayEvent.setAmount(BigDecimal.valueOf(charge.getAmountRefunded())
                            .movePointLeft(2)
                            .setScale(2, RoundingMode.HALF_UP));
                    if (charge.getCurrency() != null) {
                        gatewayEvent.setCurrency(charge.getCurrency().toUpperCase());
                    }
                    break;
                default:
                    gatewayEvent.setEventType("UNKNOWN");
            }

            return gatewayEvent;

        } catch (Exception e) {
            log.error("Stripe webhook verification failed: {}", e.getMessage());
            throw new WebhookSignatureException("STRIPE", "Stripe signature verification failed", e);
        }
    }
}
