package com.ecommerce.payment.razorpay;

import com.ecommerce.payment.dto.CheckoutSessionRequest;
import com.ecommerce.payment.dto.CheckoutSessionResponse;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class RazorpayPaymentGateway implements PaymentGateway {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private RazorpayClient client;

    @PostConstruct
    public void init() {
        try {
            this.client = new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            log.error("Failed to initialize Razorpay client", e);
        }
    }

    @Override
    public String getGatewayType() {
        return "RAZORPAY";
    }

    @Override
    public CheckoutSessionResponse createCheckoutSession(CheckoutSessionRequest request) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount().multiply(new BigDecimal(100)).longValue()); // in paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", request.getOrderId());
            orderRequest.put("payment_capture", 1); // Auto-capture

            Order order = client.orders.create(orderRequest);

            log.info("Razorpay order created: {} for receipt: {}", order.get("id"), request.getOrderId());

            return CheckoutSessionResponse.builder()
                    .sessionId(order.get("id"))
                    .clientSecret(order.get("id")) // In Razorpay, the order_id is used similarly to client_secret for
                                                   // initialization
                    .checkoutUrl("razorpay_order_id:" + order.get("id"))
                    .gatewayType("RAZORPAY")
                    .build();

        } catch (RazorpayException e) {
            log.error("Razorpay error: {}", e.getMessage());
            throw new RuntimeException("Razorpay failure", e);
        }
    }

    @Override
    public String createPaymentIntent(String orderId, BigDecimal amount, String currency) {
        CheckoutSessionRequest req = CheckoutSessionRequest.builder()
                .orderId(orderId)
                .amount(amount)
                .currency(currency)
                .build();
        return createCheckoutSession(req).getSessionId();
    }

    @Override
    public void processRefund(String transactionId, BigDecimal amount, String reason) {
        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("payment_id", transactionId);
            refundRequest.put("amount", amount.multiply(new BigDecimal(100)).longValue());

            client.payments.refund(refundRequest);
            log.info("Razorpay refund processed for payment: {}", transactionId);
        } catch (RazorpayException e) {
            log.error("Razorpay refund error: {}", e.getMessage());
            throw new RuntimeException("Razorpay refund failure", e);
        }
    }
}
