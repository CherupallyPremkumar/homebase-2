package com.homebase.ecom.inventory.domain.model;

import java.time.Instant;

public record DamageRecord(
    String unitIdentifier,
    String location,
    String damageType,
    String description,
    Instant discoveredAt,
    DamageStatus status
) {}
