package com.homebase.ecom.policy.service.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homebase.ecom.policy.api.dto.PolicyDto;

import java.util.List;

@RestController
@ChenileController(value = "policyQueryService", serviceName = "policyService")
public class PolicyQueryController extends ControllerSupport {

    @GetMapping("/policies")
    public ResponseEntity<GenericResponse<List<PolicyDto>>> listPolicies(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/policies/{id}")
    public ResponseEntity<GenericResponse<PolicyDto>> getPolicy(
            HttpServletRequest request,
            @PathVariable String id) {
        return process(request, id);
    }

    @PutMapping("/policies/{id}")
    public ResponseEntity<GenericResponse<PolicyDto>> updatePolicy(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody PolicyDto dto) {
        return process(request, id, dto);
    }
}
