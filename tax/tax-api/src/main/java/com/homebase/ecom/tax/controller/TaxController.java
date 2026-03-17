package com.homebase.ecom.tax.controller;

import com.homebase.ecom.tax.dto.TaxCalculationRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "taxService", serviceName = "taxServiceImpl")
@Tag(name = "Tax", description = "GST tax calculation engine")
public class TaxController extends ControllerSupport {

    @PostMapping("/tax/_calculate")
    public ResponseEntity<GenericResponse<Object>> calculateTax(
            HttpServletRequest request,
            @RequestBody TaxCalculationRequestDTO body) {
        return process(request, body);
    }
}
