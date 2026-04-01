package com.homebase.ecom.cms.banner.configuration.controller;

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
import com.homebase.ecom.cms.model.Banner;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsBannerService", serviceName = "_cmsBannerStateEntityService_", healthCheckerName = "cmsBannerHealthChecker")
@Tag(name = "CMS Banners", description = "Banner lifecycle management")
public class CmsBannerController extends ControllerSupport {

	@GetMapping("/cms-banner/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Banner>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/cms-banner")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Banner>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Banner entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/cms-banner/{id}/{eventID}")
	@BodyTypeSelector("cmsBannerBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Banner>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
