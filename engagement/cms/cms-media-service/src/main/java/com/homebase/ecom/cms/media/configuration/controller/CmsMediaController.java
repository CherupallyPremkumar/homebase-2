package com.homebase.ecom.cms.media.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.homebase.ecom.cms.dto.request.CreateMediaRequest;
import com.homebase.ecom.cms.dto.response.MediaResult;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsMediaService", serviceName = "cmsMediaServiceImpl")
@Tag(name = "CMS Media", description = "Media asset management")
public class CmsMediaController extends ControllerSupport {

	@PostMapping("/cms-media")
	public ResponseEntity<GenericResponse<MediaResult>> create(
			HttpServletRequest httpServletRequest,
			@RequestBody CreateMediaRequest media) {
		return process(httpServletRequest, media);
	}

	@GetMapping("/cms-media/{id}")
	public ResponseEntity<GenericResponse<MediaResult>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@DeleteMapping("/cms-media/{id}")
	public ResponseEntity<GenericResponse<Void>> delete(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}
}
