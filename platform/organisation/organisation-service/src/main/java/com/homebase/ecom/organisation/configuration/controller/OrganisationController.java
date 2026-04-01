package com.homebase.ecom.organisation.configuration.controller;

import com.homebase.ecom.organisation.model.Organisation;
import com.homebase.ecom.organisation.service.OrganisationService;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ChenileController(value = "organisationService", serviceName = "organisationService")
public class OrganisationController extends ControllerSupport {

    @PostMapping("/organisation")
    public ResponseEntity<GenericResponse<Organisation>> create(
            HttpServletRequest request, @RequestBody Organisation organisation) {
        return process(request, organisation);
    }

    @GetMapping("/organisation/{id}")
    public ResponseEntity<GenericResponse<Organisation>> retrieve(
            HttpServletRequest request, @PathVariable String id) {
        return process(request, id);
    }

    @GetMapping("/organisation/tenant/{tenantId}")
    public ResponseEntity<GenericResponse<Organisation>> retrieveByTenant(
            HttpServletRequest request, @PathVariable String tenantId) {
        return process(request, tenantId);
    }

    @PutMapping("/organisation/{id}")
    public ResponseEntity<GenericResponse<Organisation>> update(
            HttpServletRequest request, @PathVariable String id, @RequestBody Organisation organisation) {
        return process(request, id, organisation);
    }
}
