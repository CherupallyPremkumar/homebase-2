package com.homebase.ecom.demonotification.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.annotation.EventsSubscribedTo;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.demonotification.model.DemoNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for DemoNotification.
 * The @EventsSubscribedTo annotation subscribes to events published by
 * demo-order on the "demo-order.events" topic. This is the ONLY connection
 * between the two modules -- zero compile-time dependency.
 */
@RestController
@ChenileController(value = "demoNotifService", serviceName = "_demoNotifStateEntityService_")
public class DemoNotificationController extends ControllerSupport {

    @PostMapping("/demo-notif")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoNotification>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody DemoNotification entity) {
        return process(httpServletRequest, entity);
    }

    @GetMapping("/demo-notif/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoNotification>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PatchMapping("/demo-notif/{id}/{eventID}")
    @BodyTypeSelector("demoNotifBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoNotification>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }

    /**
     * Receives cross-BC events from the demo-order module.
     * Chenile auto-subscribes to "demo-order.events" via @EventsSubscribedTo.
     * In InVM mode, EventProcessor.handleEvent routes here directly.
     * In Kafka mode, ChenilePubSubEntryPoint routes here via Kafka consumer.
     */
    @EventsSubscribedTo({"demo-order.events"})
    @PostMapping("/demo-notif/on-event")
    public ResponseEntity<GenericResponse<Void>> onOrderEvent(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(Map.class) @RequestBody Map<String, Object> eventPayload) {
        return process(httpServletRequest, eventPayload);
    }
}
