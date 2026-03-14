package com.homebase.ecom.support.configuration.controller;

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
import com.homebase.ecom.support.model.SupportTicket;

@RestController
@ChenileController(value = "supportService", serviceName = "_supportStateEntityService_", healthCheckerName = "supportHealthChecker")
public class SupportController extends ControllerSupport {

	@GetMapping("/support/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupportTicket>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/support")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupportTicket>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody SupportTicket entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/support/{id}/{eventID}")
	@BodyTypeSelector("supportBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<SupportTicket>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}
}
