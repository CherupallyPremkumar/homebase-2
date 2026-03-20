package com.homebase.ecom.offer.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.security.model.SecurityConfig;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.homebase.ecom.offer.domain.model.Offer;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "offerService", serviceName = "_offerStateEntityService_")
@Tag(name = "Offer", description = "Product offer management")
public class OfferController extends ControllerSupport {

    @GetMapping("/offer/{id}")
    @SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Offer>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/offer")
    @SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Offer>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody Offer entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/offer/{id}/{eventID}")
    @BodyTypeSelector("offerBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "offerEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Offer>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
