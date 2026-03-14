package com.homebase.ecom.cart.configuration.controller;

import java.util.Map;

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
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.infrastructure.mapper.CartDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@ChenileController(value = "cartService", serviceName = "_cartStateEntityService_", healthCheckerName = "cartHealthChecker")
public class CartController extends ControllerSupport {

	@Autowired
	private CartDtoMapper cartDtoMapper;

	@GetMapping("/cart/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CartDto>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> response = process(httpServletRequest, id);
		return transform(response);
	}

	@PostMapping("/cart")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CartDto>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Cart entity) {
		ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> response = process(httpServletRequest,
				entity);
		return transform(response);
	}

	@PatchMapping("/cart/{id}/{eventID}")
	@BodyTypeSelector("cartBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<CartDto>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> response = process(httpServletRequest, id,
				eventID, eventPayload);
		return transform(response);
	}

	private ResponseEntity<GenericResponse<StateEntityServiceResponse<CartDto>>> transform(
			ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> response) {
		GenericResponse<StateEntityServiceResponse<Cart>> body = response.getBody();
		if (body == null || body.getData() == null) {
			return ResponseEntity.status(response.getStatusCode()).build();
		}

		StateEntityServiceResponse<Cart> sesr = body.getData();
		CartDto cartDto = cartDtoMapper.toDto(sesr.getMutatedEntity());

		StateEntityServiceResponse<CartDto> sesrDto = new StateEntityServiceResponse<>();
		sesrDto.setMutatedEntity(cartDto);
		sesrDto.setAllowedActionsAndMetadata(sesr.getAllowedActionsAndMetadata());

		GenericResponse<StateEntityServiceResponse<CartDto>> genericResponseDto = new GenericResponse<>(sesrDto);
		genericResponseDto.setSuccess(body.isSuccess());
		genericResponseDto.setCode(body.getCode());

		if (body.getErrors() != null) {
			for (org.chenile.base.response.ResponseMessage error : body.getErrors()) {
				genericResponseDto.addWarningMessage(error);
			}
		}

		return new ResponseEntity<>(genericResponseDto, response.getHeaders(), response.getStatusCode());
	}

}
