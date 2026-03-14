package com.homebase.ecom.inventory.domain.model;

import java.time.Instant;

public record Reservation(
    String orderId,
    String sessionId,
    Integer quantity,
    Instant reservedAt,
    Instant expiresAt,
    ReservationStatus status
) {}
