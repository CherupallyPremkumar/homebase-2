package com.homebase.ecom.promo.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ChenileController(value = "promoService", serviceName = "_promoStateEntityService_")
public class PromoController extends ControllerSupport {

    @GetMapping("/promo/{id}")
    public ResponseEntity<GenericResponse<Object>> getById(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PostMapping("/promo")
    public ResponseEntity<GenericResponse<Object>> create(
            HttpServletRequest request,
            @RequestBody Object entity) {
        return process(request, entity);
    }

    @PatchMapping("/promo/{id}/{eventID}")
    public ResponseEntity<GenericResponse<Object>> processById(
            HttpServletRequest request,
            @PathVariable String id,
            @PathVariable String eventID,
            @RequestBody Object payload) {
        return process(request, id, eventID, payload);
    }
}
