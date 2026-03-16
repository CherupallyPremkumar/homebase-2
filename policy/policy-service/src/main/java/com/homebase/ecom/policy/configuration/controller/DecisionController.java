package com.homebase.ecom.policy.configuration.controller;

import com.homebase.ecom.policy.api.dto.EvaluateRequest;
import com.homebase.ecom.policy.api.dto.DecisionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chenile controller for policy evaluation.
 * Exposes the DecisionService to other bounded contexts via Chenile proxy.
 */
@ChenileController(value = "decisionService", serviceName = "decisionService")
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Policy", description = "Business policy engine")
public class DecisionController extends ControllerSupport {

    @PostMapping("/policy/evaluate")
    public ResponseEntity<GenericResponse<DecisionDto>> evaluate(
            HttpServletRequest httpServletRequest,
            @RequestBody EvaluateRequest request) {
        return process(httpServletRequest, request);
    }

}
