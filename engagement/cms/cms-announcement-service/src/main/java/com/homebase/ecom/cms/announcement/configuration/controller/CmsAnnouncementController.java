package com.homebase.ecom.cms.announcement.configuration.controller;

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
import com.homebase.ecom.cms.model.Announcement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsAnnouncementService", serviceName = "_cmsAnnouncementStateEntityService_", healthCheckerName = "cmsAnnouncementHealthChecker")
@Tag(name = "CMS Announcements", description = "Announcement lifecycle management")
public class CmsAnnouncementController extends ControllerSupport {

	@GetMapping("/cms-announcement/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Announcement>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/cms-announcement")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Announcement>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Announcement entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/cms-announcement/{id}/{eventID}")
	@BodyTypeSelector("cmsAnnouncementBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Announcement>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

}
