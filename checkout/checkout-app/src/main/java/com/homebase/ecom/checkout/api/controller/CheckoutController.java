package com.homebase.ecom.checkout.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.checkout.api.dto.request.CheckoutInitiateRequest;
import com.homebase.ecom.checkout.domain.model.Checkout;

@RestController
@ChenileController(value = "checkoutService", serviceName = "checkoutService")
public class CheckoutController extends ControllerSupport {

    @PostMapping("/checkout/_initiate")
    public ResponseEntity<GenericResponse<Checkout>> initiateCheckout(
            HttpServletRequest request,
            @RequestBody CheckoutInitiateRequest body) {
        return process(request, body);
    }
}
