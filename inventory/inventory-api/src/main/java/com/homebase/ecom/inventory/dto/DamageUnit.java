package com.homebase.ecom.inventory.dto;

/**
 * Represents a single damaged unit within a damage event payload.
 * Used by returnDamaged and damageFound events to identify which specific units are damaged.
 */
public class DamageUnit {
    private String unitIdentifier;
    private String location;
    private String damageType;
    private String description;

    public String getUnitIdentifier() { return unitIdentifier; }
    public void setUnitIdentifier(String unitIdentifier) { this.unitIdentifier = unitIdentifier; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDamageType() { return damageType; }
    public void setDamageType(String damageType) { this.damageType = damageType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
