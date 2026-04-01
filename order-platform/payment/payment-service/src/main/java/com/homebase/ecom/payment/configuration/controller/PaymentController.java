package com.homebase.ecom.payment.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.pubsub.model.ChenilePubSub;
import org.chenile.security.model.SecurityConfig;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.payment.domain.model.Payment;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Payment STM lifecycle operations.
 * Event-driven payment module — primarily consumed via Kafka, but
 * exposes REST endpoints for admin/operational use and BDD testing.
 */
@RestController
@ChenilePubSub
@ChenileController(value = "paymentService", serviceName = "_paymentStateEntityService_",
        healthCheckerName = "paymentHealthChecker")
@Tag(name = "Payment", description = "Payment lifecycle management")
public class PaymentController extends ControllerSupport {

    @GetMapping("/payment/{id}")
    @SecurityConfig(authorities = {"some_premium_scope", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Payment>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/payment")
    @SecurityConfig(authorities = {"some_premium_scope", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Payment>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody Payment entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/payment/{id}/{eventID}")
    @BodyTypeSelector("paymentBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "paymentEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Payment>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
