package com.homebase.ecom.supplier.configuration.controller;

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
import com.homebase.ecom.supplier.model.Supplier;
import org.chenile.security.model.SecurityConfig;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "supplierService", serviceName = "_supplierStateEntityService_", healthCheckerName = "supplierHealthChecker")
@Tag(name = "Supplier", description = "Supplier lifecycle management")
public class SupplierController extends ControllerSupport {

	/**
	 * Apply: SELLER creates a new supplier application (initial state APPLIED).
	 */
	@PostMapping("/supplier")
	@SecurityConfig(authorities = { "SELLER", "ADMIN", "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplier>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Supplier entity) {
		return process(httpServletRequest, entity);
	}

	@GetMapping("/supplier/{id}")
	@SecurityConfig(authorities = { "SELLER", "ADMIN", "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplier>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	/**
	 * Process state transitions. ACLs are event-level (defined in supplier-states.xml).
	 */
	@PatchMapping("/supplier/{id}/{eventID}")
	@BodyTypeSelector("supplierBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "supplierEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplier>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
