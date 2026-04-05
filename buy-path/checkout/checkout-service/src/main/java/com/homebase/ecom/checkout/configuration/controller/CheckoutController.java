package com.homebase.ecom.checkout.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.annotation.EventsSubscribedTo;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.security.model.SecurityConfig;
import org.springframework.http.ResponseEntity;

import org.chenile.stm.StateEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.checkout.model.Checkout;

@RestController
@ChenileController(value = "checkoutService", serviceName = "_checkoutStateEntityService_")
public class CheckoutController extends ControllerSupport {

    @GetMapping("/checkout/{id}")
    @SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Checkout>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/checkout")
    @SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Checkout>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody Checkout entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/checkout/{id}/{eventID}")
    @BodyTypeSelector("checkoutBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "checkoutEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Checkout>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }

    /**
     * Receives cross-BC events from payment module.
     * Chenile auto-subscribes to these topics via @EventsSubscribedTo.
     * Works with both InVM (EventProcessor) and Kafka (CustomKafkaConsumer).
     *
     * The service layer parses the event, extracts checkoutId + STM eventId,
     * and calls processById internally.
     */
    @EventsSubscribedTo({"payment.events"})
    @PostMapping("/checkout/on-event")
    public ResponseEntity<GenericResponse<Void>> onExternalEvent(
            HttpServletRequest httpServletRequest,
            @RequestBody String eventPayload) {
        return process(httpServletRequest, eventPayload);
    }
}
