package com.homebase.ecom.supplierlifecycle.configuration.controller;

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
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;

@RestController
@ChenileController(value = "supplierLifecycleService", serviceName = "_supplierLifecycleStateEntityService_")
public class SupplierLifecycleController extends ControllerSupport {

    @GetMapping("/supplier-lifecycle/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupplierLifecycleSaga>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/supplier-lifecycle")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupplierLifecycleSaga>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody SupplierLifecycleSaga entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/supplier-lifecycle/{id}/{eventID}")
    @BodyTypeSelector("supplierLifecycleBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupplierLifecycleSaga>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
