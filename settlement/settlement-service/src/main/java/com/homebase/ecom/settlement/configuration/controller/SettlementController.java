package com.homebase.ecom.settlement.configuration.controller;

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
import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.security.model.SecurityConfig;

@RestController
@ChenileController(value = "settlementService", serviceName = "_settlementStateEntityService_", healthCheckerName = "settlementHealthChecker")
public class SettlementController extends ControllerSupport {

	@GetMapping("/settlement/{id}")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Settlement>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/settlement")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Settlement>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Settlement entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/settlement/{id}/{eventID}")
	@BodyTypeSelector("settlementBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "settlementEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Settlement>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
