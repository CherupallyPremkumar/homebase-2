package com.homebase.ecom.demoorder.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.demoorder.model.DemoOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ChenileController(value = "demoOrderService", serviceName = "_demoOrderStateEntityService_")
public class DemoOrderController extends ControllerSupport {

    @PostMapping("/demo-order")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoOrder>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody DemoOrder entity) {
        return process(httpServletRequest, entity);
    }

    @GetMapping("/demo-order/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoOrder>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PatchMapping("/demo-order/{id}/{eventID}")
    @BodyTypeSelector("demoOrderBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<DemoOrder>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
