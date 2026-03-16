package com.homebase.ecom.pricing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;

@RestController
@ChenileController(value = "pricingService", serviceName = "pricingServiceImpl")
@Tag(name = "Pricing", description = "Dynamic pricing engine")
public class PricingController extends ControllerSupport {

    @PostMapping("/pricing/_calculate")
    public ResponseEntity<GenericResponse<Object>> calculatePrice(
            HttpServletRequest request,
            @RequestBody PricingRequestDTO body) {
        return process(request, body);
    }

    @PostMapping("/pricing/_lock-price")
    public ResponseEntity<GenericResponse<Object>> lockPrice(
            HttpServletRequest request,
            @RequestBody PricingRequestDTO body) {
        return process(request, body);
    }
}
