package com.homebase.ecom.fulfillment.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;

import org.chenile.stm.StateEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.fulfillment.model.FulfillmentSaga;

@RestController
@ChenileController(value = "fulfillmentService", serviceName = "_fulfillmentStateEntityService_")
public class FulfillmentController extends ControllerSupport {

    @GetMapping("/fulfillment/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<FulfillmentSaga>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/fulfillment")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<FulfillmentSaga>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody FulfillmentSaga entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/fulfillment/{id}/{eventID}")
    @BodyTypeSelector("fulfillmentBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<FulfillmentSaga>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
