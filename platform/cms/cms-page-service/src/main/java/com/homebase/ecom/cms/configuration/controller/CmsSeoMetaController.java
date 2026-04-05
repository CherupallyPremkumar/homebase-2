package com.homebase.ecom.cms.configuration.controller;

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
import com.homebase.ecom.cms.dto.request.CreateSeoMetaRequest;
import com.homebase.ecom.cms.dto.response.SeoMetaResult;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cmsSeoMetaService", serviceName = "cmsSeoMetaServiceImpl")
@Tag(name = "CMS SEO Meta", description = "SEO metadata management for CMS pages")
public class CmsSeoMetaController extends ControllerSupport {

	@PostMapping("/cms-seo-meta")
	public ResponseEntity<GenericResponse<SeoMetaResult>> create(
			HttpServletRequest httpServletRequest,
			@RequestBody CreateSeoMetaRequest cmsSeoMeta) {
		return process(httpServletRequest, cmsSeoMeta);
	}

	@GetMapping("/cms-seo-meta/{id}")
	public ResponseEntity<GenericResponse<SeoMetaResult>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@DeleteMapping("/cms-seo-meta/{id}")
	public ResponseEntity<GenericResponse<Void>> delete(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}
}
