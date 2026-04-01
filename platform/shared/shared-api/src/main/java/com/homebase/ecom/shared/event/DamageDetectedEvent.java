package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when damaged stock is detected during inspection.
 */
public class DamageDetectedEvent implements Serializable {

    public static final String EVENT_TYPE = "DAMAGE_DETECTED";

    private String inventoryId;
    private String productId;
    private int damagedQuantity;
    private double damagePercentage;
    private boolean severe;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public DamageDetectedEvent() {
    }

    public DamageDetectedEvent(String inventoryId, String productId, int damagedQuantity,
            double damagePercentage, boolean severe) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.damagedQuantity = damagedQuantity;
        this.damagePercentage = damagePercentage;
        this.severe = severe;
    }

    // --- Getters & Setters ---

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getDamagedQuantity() {
        return damagedQuantity;
    }

    public void setDamagedQuantity(int damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
    }

    public double getDamagePercentage() {
        return damagePercentage;
    }

    public void setDamagePercentage(double damagePercentage) {
        this.damagePercentage = damagePercentage;
    }

    public boolean isSevere() {
        return severe;
    }

    public void setSevere(boolean severe) {
        this.severe = severe;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
