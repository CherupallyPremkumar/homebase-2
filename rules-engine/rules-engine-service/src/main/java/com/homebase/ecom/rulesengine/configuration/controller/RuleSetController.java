package com.homebase.ecom.rulesengine.configuration.controller;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "ruleSetService", serviceName = "ruleSetStateEntityService", healthCheckerName = "ruleSetHealthChecker")
@Tag(name = "RuleSet", description = "Business rules engine")
public class RuleSetController extends ControllerSupport {

    @PostMapping("/ruleSet")
    @SecurityConfig(authorities = {"ADMIN", "POLICY_ADMIN", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody RuleSet entity) {
        return process(httpServletRequest, entity);
    }

    @GetMapping("/ruleSet/{id}")
    @SecurityConfig(authorities = {"ADMIN", "POLICY_ADMIN", "SYSTEM", "test.premium"})
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PatchMapping("/ruleSet/{id}/{eventID}")
    @BodyTypeSelector("ruleSetBodyTypeSelector")
    @SecurityConfig(authoritiesSupplier = "ruleSetEventAuthoritiesSupplier")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
