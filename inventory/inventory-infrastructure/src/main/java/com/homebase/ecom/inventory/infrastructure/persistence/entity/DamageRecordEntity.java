package com.homebase.ecom.inventory.infrastructure.persistence.entity;

import java.time.Instant;
import com.homebase.ecom.inventory.domain.model.DamageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class DamageRecordEntity {
    @Column(name = "unit_identifier")
    private String unitIdentifier;

    @Column(name = "location")
    private String location;

    @Column(name = "damage_type")
    private String damageType;

    @Column(name = "description")
    private String description;

    @Column(name = "discovered_at")
    private Instant discoveredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DamageStatus status;

    // Getters and Setters
    public String getUnitIdentifier() { return unitIdentifier; }
    public void setUnitIdentifier(String unitIdentifier) { this.unitIdentifier = unitIdentifier; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDamageType() { return damageType; }
    public void setDamageType(String damageType) { this.damageType = damageType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getDiscoveredAt() { return discoveredAt; }
    public void setDiscoveredAt(Instant discoveredAt) { this.discoveredAt = discoveredAt; }

    public DamageStatus getStatus() { return status; }
    public void setStatus(DamageStatus status) { this.status = status; }
}
