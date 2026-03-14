package com.homebase.ecom.inventory.domain.model;

import java.time.Instant;

public record StockMovement(
    MovementType type,
    Integer quantity,
    String referenceId,
    String fulfillmentCenterId,
    Instant movementTime,
    String reason
) {}
