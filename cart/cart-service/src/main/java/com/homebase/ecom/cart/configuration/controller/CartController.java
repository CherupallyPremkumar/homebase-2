package com.homebase.ecom.cart.configuration.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.annotation.EventsSubscribedTo;
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
import com.homebase.ecom.cart.model.Cart;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ChenileController(value = "cartService", serviceName = "_cartStateEntityService_", healthCheckerName = "cartHealthChecker")
@Tag(name = "Cart", description = "Shopping cart management")
public class CartController extends ControllerSupport {

	@GetMapping("/cart/{id}")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id) {
		return process(httpServletRequest, id);
	}

	@PostMapping("/cart")
	@SecurityConfig(authorities = { "some_premium_scope", "test.premium" })
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class) @RequestBody Cart entity) {
		return process(httpServletRequest, entity);
	}

	@PatchMapping("/cart/{id}/{eventID}")
	@BodyTypeSelector("cartBodyTypeSelector")
	@SecurityConfig(authoritiesSupplier = "cartEventAuthoritiesSupplier")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Cart>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) @RequestBody String eventPayload) {
		return process(httpServletRequest, id, eventID, eventPayload);
	}

	/**
	 * Receives cross-BC events from checkout, inventory, product, and promo modules.
	 * Chenile auto-subscribes to these topics via @EventsSubscribedTo.
	 * Works with both InVM (EventProcessor) and Kafka (CustomKafkaConsumer).
	 *
	 * The service layer parses the event, extracts cartId + STM eventId,
	 * and calls processById internally.
	 */
	@EventsSubscribedTo({"checkout.events", "inventory.events", "product.events", "promo.events"})
	@PostMapping("/cart/on-event")
	public ResponseEntity<GenericResponse<Void>> onExternalEvent(
			HttpServletRequest httpServletRequest,
			@RequestBody String eventPayload) {
		return process(httpServletRequest, eventPayload);
	}
}
