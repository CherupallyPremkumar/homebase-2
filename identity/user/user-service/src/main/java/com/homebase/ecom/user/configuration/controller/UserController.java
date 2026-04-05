package com.homebase.ecom.user.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.security.model.SecurityConfig;
import org.springframework.http.ResponseEntity;

import org.chenile.stm.StateEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.user.domain.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "userService", serviceName = "_userStateEntityService_", healthCheckerName = "userHealthChecker")
@Tag(name = "User", description = "User account management")
public class UserController extends ControllerSupport {

	@GetMapping("/user/{id}")
	@SecurityConfig(authorities = { "CUSTOMER", "ADMIN" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<User>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/user")
	@SecurityConfig(authorities = { "CUSTOMER", "ADMIN" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<User>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody User entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/user/{id}/{eventID}")
	@BodyTypeSelector("userBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "userEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<User>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
