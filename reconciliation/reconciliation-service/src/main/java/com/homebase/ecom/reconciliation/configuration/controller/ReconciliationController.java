package com.homebase.ecom.reconciliation.configuration.controller;

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
import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import org.chenile.security.model.SecurityConfig;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "reconciliationService", serviceName = "_reconciliationStateEntityService_", healthCheckerName = "reconciliationHealthChecker")
@Tag(name = "Reconciliation", description = "Payment reconciliation batch management")
public class ReconciliationController extends ControllerSupport {

    @GetMapping("/reconciliation/{id}")
    @SecurityConfig(authorities = { "FINANCE", "ADMIN" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReconciliationBatch>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/reconciliation")
    @SecurityConfig(authorities = { "SYSTEM", "FINANCE" })
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReconciliationBatch>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody ReconciliationBatch entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/reconciliation/{id}/{eventID}")
    @BodyTypeSelector("reconciliationBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "reconciliationEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<ReconciliationBatch>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
