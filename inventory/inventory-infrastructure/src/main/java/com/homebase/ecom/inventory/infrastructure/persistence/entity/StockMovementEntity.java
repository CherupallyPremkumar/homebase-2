package com.homebase.ecom.inventory.infrastructure.persistence.entity;

import java.time.Instant;
import com.homebase.ecom.inventory.domain.model.MovementType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class StockMovementEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MovementType type;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "reference_id")
    private String referenceId;
    
    @Column(name = "fulfillment_center_id")
    private String fulfillmentCenterId;
    
    @Column(name = "movement_time")
    private Instant movementTime;
    
    @Column(name = "reason")
    private String reason;

    // Getters and Setters
    public MovementType getType() { return type; }
    public void setType(MovementType type) { this.type = type; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public String getFulfillmentCenterId() { return fulfillmentCenterId; }
    public void setFulfillmentCenterId(String fulfillmentCenterId) { this.fulfillmentCenterId = fulfillmentCenterId; }

    public Instant getMovementTime() { return movementTime; }
    public void setMovementTime(Instant movementTime) { this.movementTime = movementTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
