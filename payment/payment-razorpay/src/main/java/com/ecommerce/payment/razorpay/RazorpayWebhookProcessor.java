package com.ecommerce.payment.razorpay;

import com.ecommerce.payment.exception.WebhookSignatureException;
import com.ecommerce.payment.gateway.GatewayEvent;
import com.ecommerce.payment.gateway.WebhookProcessor;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class RazorpayWebhookProcessor implements WebhookProcessor {

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

            if ("order.paid".equals(event)) {
                JSONObject orderJson = payloadJson.getJSONObject("order").getJSONObject("entity");
                gatewayEvent.setEventType("PAYMENT_SUCCESS");
                gatewayEvent.setOrderId(orderJson.getString("receipt"));
                gatewayEvent.setGatewayTransactionId(orderJson.getString("id"));
                // Razorpay amounts are in the smallest currency unit (paise)
                // Internally we store amounts as NUMERIC(10,2) in rupees.
                gatewayEvent.setAmount(BigDecimal.valueOf(orderJson.getLong("amount_paid"))
                        .movePointLeft(2)
                        .setScale(2, RoundingMode.HALF_UP));
            } else if ("payment.failed".equals(event)) {
                gatewayEvent.setEventType("PAYMENT_FAILED");
                // Best-effort extraction (payload shape can vary by event)
                JSONObject paymentEntity = payloadJson.optJSONObject("payment") != null
                        ? payloadJson.optJSONObject("payment").optJSONObject("entity")
                        : null;
                if (paymentEntity != null) {
                    // Razorpay payment entity often includes an order_id
                    gatewayEvent.setGatewayTransactionId(paymentEntity.optString("order_id", ""));
                    // orderId mapping not always present; leave null if unavailable
                }
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
     * This ensures that if Razorpay retries the same webhook, we get the same event ID.
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
                if (hex.length() == 1) hexString.append('0');
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
