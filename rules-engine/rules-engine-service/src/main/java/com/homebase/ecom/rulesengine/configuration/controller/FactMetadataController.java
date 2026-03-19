package com.homebase.ecom.rulesengine.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.security.model.SecurityConfig;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.homebase.ecom.rulesengine.api.dto.FactDefinitionDto;

import java.util.List;

@RestController
@ChenileController(value = "factMetadataService", serviceName = "factMetadataService")
@Tag(name = "RuleSet", description = "Business rules engine")
public class FactMetadataController extends ControllerSupport {

    @GetMapping("/factMetadata")
    @SecurityConfig(authorities = {"ADMIN", "POLICY_ADMIN", "SYSTEM"})
    public ResponseEntity<GenericResponse<List<FactDefinitionDto>>> getAllFacts(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/factMetadata/_byModule")
    @SecurityConfig(authorities = {"ADMIN", "POLICY_ADMIN", "SYSTEM"})
    public ResponseEntity<GenericResponse<List<FactDefinitionDto>>> getFactsByModule(
            HttpServletRequest request,
            @RequestParam String module) {
        return process(request, module);
    }
}
