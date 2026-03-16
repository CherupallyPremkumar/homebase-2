package com.homebase.ecom.returnprocessing.configuration.controller;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for return processing saga using STM pattern.
 * GET /return-processing/{id} - Retrieve saga state
 * POST /return-processing - Create a new saga
 * PATCH /return-processing/{id}/{eventID} - Trigger a state transition
 */
@RestController
@ChenileController(value = "returnProcessingService",
        serviceName = "_returnProcessingStateEntityService_",
        healthCheckerName = "returnProcessingHealthChecker")
@Tag(name = "Return Processing", description = "Return processing saga")
public class ReturnProcessingController extends ControllerSupport {

    @GetMapping("/return-processing/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReturnProcessingSaga>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/return-processing")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReturnProcessingSaga>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody ReturnProcessingSaga entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/return-processing/{id}/{eventID}")
    @BodyTypeSelector("returnProcessingBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReturnProcessingSaga>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
