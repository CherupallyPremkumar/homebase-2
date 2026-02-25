package com.homebase.ecom.supplierproduct.configuration.controller;

import java.util.Map;

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
import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import org.chenile.security.model.SecurityConfig;

@RestController
@ChenileController(value = "supplierproductService", serviceName = "_supplierproductStateEntityService_", healthCheckerName = "supplierproductHealthChecker")
public class SupplierproductController extends ControllerSupport {

	@GetMapping("/supplierproduct/{id}")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplierproduct>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/supplierproduct")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplierproduct>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Supplierproduct entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/supplierproduct/{id}/{eventID}")
	@BodyTypeSelector("supplierproductBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "supplierproductEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Supplierproduct>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
