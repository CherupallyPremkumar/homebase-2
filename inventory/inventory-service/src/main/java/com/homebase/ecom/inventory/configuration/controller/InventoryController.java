package com.homebase.ecom.inventory.configuration.controller;

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
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.security.model.SecurityConfig;

@RestController
@ChenileController(value = "inventoryService", serviceName = "_inventoryStateEntityService_", healthCheckerName = "inventoryHealthChecker")
public class InventoryController extends ControllerSupport {

	@GetMapping("/inventory/{id}")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<InventoryItem>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/inventory")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<InventoryItem>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody InventoryItem entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/inventory/{id}/{eventID}")
	@BodyTypeSelector("inventoryBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "inventoryEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<InventoryItem>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
