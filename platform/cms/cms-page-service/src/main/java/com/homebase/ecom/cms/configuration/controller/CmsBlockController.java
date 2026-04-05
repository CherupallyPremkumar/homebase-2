package com.homebase.ecom.cms.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.homebase.ecom.cms.dto.request.CreateBlockRequest;
import com.homebase.ecom.cms.dto.response.BlockResult;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsBlockService", serviceName = "cmsBlockServiceImpl")
@Tag(name = "CMS Blocks", description = "Content block management")
public class CmsBlockController extends ControllerSupport {

	@PostMapping("/cms-block")
	public ResponseEntity<GenericResponse<BlockResult>> create(
			HttpServletRequest httpServletRequest,
			@RequestBody CreateBlockRequest block) {
		return process(httpServletRequest, block);
	}

	@GetMapping("/cms-block/{id}")
	public ResponseEntity<GenericResponse<BlockResult>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PutMapping("/cms-block/{id}")
	public ResponseEntity<GenericResponse<BlockResult>> update(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@RequestBody CreateBlockRequest block) {
		return process(httpServletRequest, id, block);
	}

	@DeleteMapping("/cms-block/{id}")
	public ResponseEntity<GenericResponse<Void>> delete(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}
}
