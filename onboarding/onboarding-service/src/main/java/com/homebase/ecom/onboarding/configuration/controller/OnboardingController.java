package com.homebase.ecom.onboarding.configuration.controller;

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
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "onboardingService", serviceName = "_onboardingStateEntityService_", healthCheckerName = "onboardingHealthChecker")
@Tag(name = "Onboarding", description = "Supplier onboarding lifecycle")
public class OnboardingController extends ControllerSupport {

    @GetMapping("/onboarding/{id}")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<OnboardingSaga>>> retrieve(
            HttpServletRequest httpServletRequest,
            @PathVariable String id) {
        return process(httpServletRequest, id);
    }

    @PostMapping("/onboarding")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<OnboardingSaga>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody OnboardingSaga entity) {
        return process(httpServletRequest, entity);
    }

    @PatchMapping("/onboarding/{id}/{eventID}")
    @BodyTypeSelector("onboardingBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<OnboardingSaga>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody String eventPayload) {
        return process(httpServletRequest, id, eventID, eventPayload);
    }
}
