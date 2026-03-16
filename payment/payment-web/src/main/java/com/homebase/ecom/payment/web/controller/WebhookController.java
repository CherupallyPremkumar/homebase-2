package com.homebase.ecom.payment.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "webhookService", serviceName = "webhookServiceImpl")
@Tag(name = "Payment Webhook", description = "Payment gateway webhook handlers")
public class WebhookController extends ControllerSupport {

    @PostMapping("/webhook/{gatewayType}")
    public ResponseEntity<GenericResponse<String>> handleWebhook(
            HttpServletRequest request,
            @PathVariable String gatewayType,
            @RequestBody String payload) {
        return process(request, gatewayType, payload);
    }
}
