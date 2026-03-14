package com.homebase.ecom.checkout.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.checkout.domain.model.Checkout;

import java.util.UUID;

@RestController
@ChenileController(value = "checkoutStatusService", serviceName = "checkoutService")
public class CheckoutStatusController extends ControllerSupport {

    @GetMapping("/checkout/{checkoutId}/_status")
    public ResponseEntity<GenericResponse<Checkout>> getCheckoutStatus(
            HttpServletRequest request,
            @PathVariable UUID checkoutId) {
        return process(request, checkoutId);
    }

    @PostMapping("/checkout/{checkoutId}/_cancel")
    public ResponseEntity<GenericResponse<Void>> cancelCheckout(
            HttpServletRequest request,
            @PathVariable UUID checkoutId) {
        return process(request, checkoutId);
    }
}
