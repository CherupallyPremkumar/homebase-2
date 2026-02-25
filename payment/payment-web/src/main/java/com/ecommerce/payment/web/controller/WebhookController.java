package com.ecommerce.payment.web.controller;

import com.ecommerce.payment.exception.DuplicateWebhookException;
import com.ecommerce.payment.exception.WebhookSignatureException;
import com.ecommerce.payment.service.impl.WebhookServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookServiceImpl webhookService;

    @PostMapping("/{gatewayType}")
    public ResponseEntity<String> handleWebhook(
            @PathVariable String gatewayType,
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String stripeSignature,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String razorpaySignature) {

        log.info("Received webhook for gateway: {}", gatewayType);

        String signature = "stripe".equalsIgnoreCase(gatewayType) ? stripeSignature : razorpaySignature;

        try {
            webhookService.processIncomingWebhook(gatewayType, payload, signature);
            return ResponseEntity.ok("Processed");
        } catch (WebhookSignatureException e) {
            // CRITICAL: Signature verification failed - potential security threat
            log.warn("Webhook signature verification failed for {}: {}", gatewayType, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        } catch (DuplicateWebhookException e) {
            // Duplicate webhook - already processed, return 200 to prevent retries
            log.info("Duplicate webhook ignored: {}", e.getEventId());
            return ResponseEntity.ok("Already processed");
        } catch (Exception e) {
            // Business logic error - return 500 so gateway will retry
            log.error("Webhook processing error for {}: {}", gatewayType, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processing failed");
        }
    }
}
