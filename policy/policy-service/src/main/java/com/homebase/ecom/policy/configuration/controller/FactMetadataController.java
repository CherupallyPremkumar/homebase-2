package com.homebase.ecom.policy.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.homebase.ecom.policy.api.dto.FactDefinitionDto;

import java.util.List;

@RestController
@ChenileController(value = "factMetadataService", serviceName = "factMetadataService")
@Tag(name = "Policy", description = "Business policy engine")
public class FactMetadataController extends ControllerSupport {

    @GetMapping("/factMetadata")
    public ResponseEntity<GenericResponse<List<FactDefinitionDto>>> getAllFacts(
            HttpServletRequest request) {
        return process(request);
    }

    @GetMapping("/factMetadata/_byModule")
    public ResponseEntity<GenericResponse<List<FactDefinitionDto>>> getFactsByModule(
            HttpServletRequest request,
            @RequestParam String module) {
        return process(request, module);
    }
}
