package com.homebase.ecom.cms.configuration.controller;

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
import com.homebase.ecom.cms.model.CmsPage;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsPageService", serviceName = "_cmsPageStateEntityService_", healthCheckerName = "cmsPageHealthChecker")
@Tag(name = "CMS Pages", description = "CMS page lifecycle management")
public class CmsPageController extends ControllerSupport {

	@GetMapping("/cms-page/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CmsPage>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/cms-page")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CmsPage>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody CmsPage entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/cms-page/{id}/{eventID}")
	@BodyTypeSelector("cmsPageBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CmsPage>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
