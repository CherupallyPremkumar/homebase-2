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
import com.homebase.ecom.compliance.model.PlatformPolicy;

@RestController
@ChenileController(value = "platformPolicyService", serviceName = "_platformPolicyStateEntityService_",
        healthCheckerName = "complianceHealthChecker")
public class PlatformPolicyController extends ControllerSupport {

    @PostMapping("/platform-policy")
    @SecurityConfig(authorities = {"POLICY_ADMIN", "SYSTEM", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<PlatformPolicy>>> create(
            HttpServletRequest request,
            @ChenileParamType(StateEntity.class) @RequestBody PlatformPolicy policy) {
        return process(request, policy);
    }

    @GetMapping("/platform-policy/{id}")
    @SecurityConfig(authorities = {"POLICY_ADMIN", "COMPLIANCE_OFFICER", "SYSTEM", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<PlatformPolicy>>> retrieve(
            HttpServletRequest request, @PathVariable String id) {
        return process(request, id);
    }

    @PatchMapping("/platform-policy/{id}/{eventID}")
    @BodyTypeSelector("platformPolicyBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "platformPolicyEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<PlatformPolicy>>> processById(
            HttpServletRequest request, @PathVariable String id,
            @PathVariable String eventID, @ChenileParamType(Object.class) @RequestBody String payload) {
        return process(request, id, eventID, payload);
    }
}
