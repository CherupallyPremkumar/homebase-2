package com.homebase.ecom.compliance.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.stm.StateEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.compliance.model.Agreement;

@RestController
@ChenileController(value = "agreementService", serviceName = "_agreementStateEntityService_",
        healthCheckerName = "complianceHealthChecker")
public class AgreementController extends ControllerSupport {

    @PostMapping("/agreement")
    @SecurityConfig(authorities = {"LEGAL_ADMIN", "SYSTEM", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Agreement>>> create(
            HttpServletRequest request,
            @ChenileParamType(StateEntity.class) @RequestBody Agreement agreement) {
        return process(request, agreement);
    }

    @GetMapping("/agreement/{id}")
    @SecurityConfig(authorities = {"LEGAL_ADMIN", "COMPLIANCE_OFFICER", "SYSTEM", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Agreement>>> retrieve(
            HttpServletRequest request, @PathVariable String id) {
        return process(request, id);
    }

    @PatchMapping("/agreement/{id}/{eventID}")
    @BodyTypeSelector("agreementBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "agreementEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<Agreement>>> processById(
            HttpServletRequest request, @PathVariable String id,
            @PathVariable String eventID, @ChenileParamType(Object.class) @RequestBody String payload) {
        return process(request, id, eventID, payload);
    }
}
