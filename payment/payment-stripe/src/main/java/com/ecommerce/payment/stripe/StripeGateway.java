package com.ecommerce.payment.stripe;

import com.ecommerce.payment.dto.CheckoutSessionRequest;
import com.ecommerce.payment.dto.CheckoutSessionResponse;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StripeGateway implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(StripeGateway.class);

    @Override
    public String getGatewayType() {
        return "STRIPE";
    }

    @Override
    public CheckoutSessionResponse createCheckoutSession(CheckoutSessionRequest request) {
        try {
            List<SessionCreateParams.LineItem> stripeLineItems = request.getItems().stream()
                    .map(item -> SessionCreateParams.LineItem.builder()
                            .setQuantity(Long.valueOf(item.getQuantity()))
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(request.getCurrency().toLowerCase())
                                    .setUnitAmount(item.getUnitPrice().multiply(new BigDecimal(100)).longValue())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(item.getProductName())
                                            .build())
                                    .build())
                            .build())
                    .collect(Collectors.toList());

            SessionCreateParams params = SessionCreateParams.builder()
                    .addExpand("payment_intent")
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .addAllLineItem(stripeLineItems)
                    .setSuccessUrl(request.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(request.getCancelUrl())
                    .putMetadata("order_id", request.getOrderId())
                    .build();

            Session session = Session.create(params, getRequestOptions(UUID.randomUUID().toString()));

            log.info("Stripe session created: {} for order: {}",
                    session.getId(), request.getOrderId());

            return CheckoutSessionResponse.builder()
                    .sessionId(session.getId())
                    .checkoutUrl(session.getUrl())
                    .gatewayType("STRIPE")
                    .build();

        } catch (StripeException e) {
            log.error("Failed to create Stripe session for order: {}, error: {}", request.getOrderId(), e.getMessage());
            throw new RuntimeException("Gateway error: " + e.getMessage(), e);
        }
    }

    @Override
    public String createPaymentIntent(String orderId, BigDecimal amount, String currency) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount.multiply(new BigDecimal(100)).longValue())
                    .setCurrency(currency.toLowerCase())
                    .addAllPaymentMethodType(List.of("card"))
                    .putMetadata("order_id", orderId)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params, getRequestOptions(UUID.randomUUID().toString()));

            log.info("Stripe PaymentIntent created: {} for order: {}", intent.getId(), orderId);
            return intent.getClientSecret();
        } catch (StripeException e) {
            log.error("Failed to create Stripe PaymentIntent for order: {}, error: {}", orderId, e.getMessage());
            throw new RuntimeException("Gateway error: " + e.getMessage(), e);
        }
    }

    @Override
    public void processRefund(String chargeId, BigDecimal amount, String reason) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setCharge(chargeId)
                    .setAmount(amount.multiply(new BigDecimal(100)).longValue())
                    .build();

            Refund.create(params, getRequestOptions(UUID.randomUUID().toString()));
            log.info("Stripe refund processed for charge: {}", chargeId);
        } catch (StripeException e) {
            log.error("Failed to process Stripe refund for charge: {}, error: {}", chargeId, e.getMessage());
            throw new RuntimeException("Gateway error: " + e.getMessage(), e);
        }
    }

    public Event constructWebhookEvent(
            String payload,
            String sigHeader,
            String webhookSecret) throws StripeException {

        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }

    private RequestOptions getRequestOptions(String idempotencyKey) {
        return RequestOptions.builder()
                .setIdempotencyKey(idempotencyKey)
                .build();
    }
}
