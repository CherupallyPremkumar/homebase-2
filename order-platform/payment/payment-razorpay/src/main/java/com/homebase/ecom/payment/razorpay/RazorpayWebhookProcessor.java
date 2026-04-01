package com.homebase.ecom.payment.razorpay;

import com.homebase.ecom.payment.exception.WebhookSignatureException;
import com.homebase.ecom.payment.gateway.GatewayEvent;
import com.homebase.ecom.payment.gateway.WebhookProcessor;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class RazorpayWebhookProcessor implements WebhookProcessor {

    private static final Logger log = LoggerFactory.getLogger(RazorpayWebhookProcessor.class);

    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    @Override
    public boolean supports(String gatewayType) {
        return "RAZORPAY".equalsIgnoreCase(gatewayType);
    }

    @Override
    public GatewayEvent process(String payload, String signature) {
        try {
            // Verify signature
            if (!Utils.verifyWebhookSignature(payload, signature, webhookSecret)) {
                throw new WebhookSignatureException("RAZORPAY", "Invalid Razorpay webhook signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            log.info("Processing Razorpay webhook: {}", event);

            GatewayEvent gatewayEvent = new GatewayEvent();

            // Generate deterministic event ID from webhook payload
            // Razorpay doesn't provide a unique event ID in the webhook payload,
            // so we create a hash from the payload content to ensure idempotency
            String eventId = generateDeterministicEventId(json);
            gatewayEvent.setEventId(eventId);

            JSONObject payloadJson = json.getJSONObject("payload");

            if ("order.paid".equals(event) || "payment.captured".equals(event)) {
                JSONObject paymentEntity = "order.paid".equals(event)
                        ? payloadJson.getJSONObject("order").getJSONObject("entity")
                        : payloadJson.getJSONObject("payment").getJSONObject("entity");

                gatewayEvent.setEventType("PAYMENT_SUCCESS");
                gatewayEvent.setOrderId(paymentEntity.optString("receipt", ""));
                gatewayEvent.setGatewayTransactionId(paymentEntity.getString("id"));

                long amount = paymentEntity.getLong("amount");
                gatewayEvent.setAmount(BigDecimal.valueOf(amount).movePointLeft(2));
                gatewayEvent.setCurrency(paymentEntity.getString("currency"));
            } else if ("payment.failed".equals(event)) {
                gatewayEvent.setEventType("PAYMENT_FAILED");
                JSONObject paymentEntity = payloadJson.getJSONObject("payment").getJSONObject("entity");
                gatewayEvent.setGatewayTransactionId(paymentEntity.getString("id"));
                gatewayEvent.setOrderId(paymentEntity.optString("receipt", ""));
            } else if ("refund.processed".equals(event)) {
                gatewayEvent.setEventType("REFUND_SUCCESS");
                JSONObject refundEntity = payloadJson.getJSONObject("refund").getJSONObject("entity");
                gatewayEvent.setGatewayTransactionId(refundEntity.getString("id"));
                gatewayEvent.setOrderId(refundEntity.optString("receipt", ""));
                gatewayEvent.setAmount(BigDecimal.valueOf(refundEntity.getLong("amount")).movePointLeft(2));
            }

            return gatewayEvent;

        } catch (WebhookSignatureException e) {
            // Re-throw signature exceptions as-is
            throw e;
        } catch (Exception e) {
            log.error("Razorpay webhook processing failed: {}", e.getMessage());
            throw new RuntimeException("Razorpay webhook failure", e);
        }
    }

    /**
     * Generate a deterministic event ID from the webhook payload.
     * This ensures that if Razorpay retries the same webhook, we get the same event
     * ID.
     */
    private String generateDeterministicEventId(JSONObject json) {
        try {
            // Create deterministic string from webhook payload
            String accountId = json.optString("account_id", "");
            String event = json.optString("event", "");

            JSONObject payloadJson = json.optJSONObject("payload");
            String entityId = "";

            if (payloadJson != null) {
                // Extract entity ID from payload (order_id, payment_id, etc.)
                JSONObject entityJson = payloadJson.optJSONObject("order");
                if (entityJson == null) {
                    entityJson = payloadJson.optJSONObject("payment");
                }
                if (entityJson != null) {
                    JSONObject entity = entityJson.optJSONObject("entity");
                    if (entity != null) {
                        entityId = entity.optString("id", "");
                    }
                }
            }

            // Create deterministic hash
            String dataToHash = accountId + "|" + event + "|" + entityId;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return "razorpay_" + hexString.toString().substring(0, 32);

        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            // Fallback: use timestamp (not ideal, but better than crashing)
            return "razorpay_" + System.currentTimeMillis();
        }
    }
}
