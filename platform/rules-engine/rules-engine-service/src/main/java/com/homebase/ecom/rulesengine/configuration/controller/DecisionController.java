package com.homebase.ecom.rulesengine.configuration.controller;

import com.homebase.ecom.rulesengine.api.dto.EvaluateRequest;
import com.homebase.ecom.rulesengine.api.dto.DecisionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@ChenileController(value = "decisionService", serviceName = "decisionService")
@RestController
@Tag(name = "RuleSet", description = "Business rules engine")
public class DecisionController extends ControllerSupport {

    @PostMapping("/ruleSet/evaluate")
    @SecurityConfig(authorities = {"ADMIN", "SYSTEM"})
    public ResponseEntity<GenericResponse<DecisionDto>> evaluate(
            HttpServletRequest httpServletRequest,
            @RequestBody EvaluateRequest request) {
        return process(httpServletRequest, request);
    }
}
