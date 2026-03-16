package com.homebase.ecom.promo.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.security.model.SecurityConfig;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.promo.model.Coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "promoService", serviceName = "_promoStateEntityService_")
@Tag(name = "Promo", description = "Promotional campaign management")
public class PromoController extends ControllerSupport {

    @GetMapping("/promo/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Coupon>>> retrieve(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PostMapping("/promo")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Coupon>>> create(
            HttpServletRequest request,
            @ChenileParamType(StateEntity.class) @RequestBody Coupon entity) {
        return process(request, entity);
    }

    @PatchMapping("/promo/{id}/{eventID}")
    @BodyTypeSelector("promoBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "promoEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Coupon>>> processById(
            HttpServletRequest request,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(request, id, eventID, eventPayload);
    }
}
