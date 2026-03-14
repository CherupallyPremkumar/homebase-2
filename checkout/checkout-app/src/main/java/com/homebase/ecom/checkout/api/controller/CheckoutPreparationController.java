package com.homebase.ecom.checkout.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.checkout.api.dto.request.PriceCalculationRequest;
import com.homebase.ecom.checkout.api.dto.request.ShippingEstimateRequest;

@RestController
@ChenileController(value = "checkoutPreparationService", serviceName = "checkoutService")
public class CheckoutPreparationController extends ControllerSupport {

    @PostMapping("/checkout/_calculate-price")
    public ResponseEntity<GenericResponse<Object>> calculatePrice(
            HttpServletRequest request,
            @RequestBody PriceCalculationRequest body) {
        return process(request, body);
    }

    @PostMapping("/checkout/_estimate-shipping")
    public ResponseEntity<GenericResponse<Object>> estimateShipping(
            HttpServletRequest request,
            @RequestBody ShippingEstimateRequest body) {
        return process(request, body);
    }
}
