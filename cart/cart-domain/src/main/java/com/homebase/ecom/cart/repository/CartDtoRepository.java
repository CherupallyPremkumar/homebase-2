package com.homebase.ecom.cart.repository;

import java.util.List;
import java.time.Instant;

import com.homebase.ecom.dto.CartDto;
import org.chenile.query.annotation.ChenileParam;
import org.chenile.query.annotation.ChenileRepositoryDefinition;
import org.chenile.query.annotation.QueryName;
import org.chenile.query.repository.ChenileRepository;

import com.homebase.ecom.dto.CartTimeoutDto;

/**
 * ChenileRepository for reading Cart data via the cart-query service.
 *
 * Query mapping (from cart.json + cart.xml):
 * "carts" → Cart.getAll → CartDto
 * "idleCarts" → Cart.getIdleCarts → CartTimeoutDto
 * "stuckCheckouts"→ Cart.getStuckCheckouts→ CartTimeoutDto
 * "stuckPayments" → Cart.getStuckPayments → CartTimeoutDto
 *
 * Follows the same pattern as UsersDtoRepository in user-service.
 */
@ChenileRepositoryDefinition(entityClass = CartDto.class)
public interface CartDtoRepository extends ChenileRepository<CartDto> {

    // ── carts (Cart.getAll) ────────────────────────────────────────────────

    /** All carts for a given user. */
    @QueryName("carts")
    List<CartDto> findByUserId(@ChenileParam("userId") String userId);

    /** Carts in a given lifecycle state (e.g., ACTIVE, CHECKOUT_IN_PROGRESS). */
    @QueryName("carts")
    List<CartDto> findByStateId(@ChenileParam("stateId") String stateId);

    /** Carts with a given payment/fulfillment status. */
    @QueryName("carts")
    List<CartDto> findByStatus(@ChenileParam("status") String status);

    // ── idleCarts (Cart.getIdleCarts) ─────────────────────────────────────

    /** Carts that have been idle since before the given time. */
    @QueryName("idleCarts")
    List<CartTimeoutDto> getIdleCarts(@ChenileParam("lastModifiedTime") Instant lastModifiedTime);

    // ── stuckCheckouts (Cart.getStuckCheckouts) ───────────────────────────

    /** Carts stuck in CHECKOUT_IN_PROGRESS since before the given time. */
    @QueryName("stuckCheckouts")
    List<CartTimeoutDto> getStuckCheckouts(@ChenileParam("lastModifiedTime") Instant lastModifiedTime);

    // ── stuckPayments (Cart.getStuckPayments) ─────────────────────────────

    /** Carts stuck in PAYMENT_PENDING since before the given time. */
    @QueryName("stuckPayments")
    List<CartTimeoutDto> getStuckPayments(@ChenileParam("lastModifiedTime") Instant lastModifiedTime);
}
