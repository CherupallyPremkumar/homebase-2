package com.homebase.ecom.checkout.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ChenileController(value = "checkoutWebhookService", serviceName = "checkoutService")
public class CheckoutWebhookController extends ControllerSupport {

    @PostMapping("/webhook/checkout/stripe")
    public ResponseEntity<GenericResponse<String>> handleStripeWebhook(
            HttpServletRequest request,
            @RequestBody String payload) {
        return process(request, payload);
    }

    @PostMapping("/webhook/checkout/payment-callback")
    public ResponseEntity<GenericResponse<String>> handleGenericPaymentCallback(
            HttpServletRequest request,
            @RequestBody Object callbackData) {
        return process(request, callbackData);
    }
}
