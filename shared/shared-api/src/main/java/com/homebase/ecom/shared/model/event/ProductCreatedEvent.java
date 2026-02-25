package com.homebase.ecom.shared.model.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a new product is created.
 */
public class ProductCreatedEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_CREATED";

    private String productId;
    private String name;
    private String eventType = EVENT_TYPE;
    private Integer initialQuantity;
    private LocalDateTime timestamp;

    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String productId, String name, Integer initialQuantity, LocalDateTime timestamp) {
        this.productId = productId;
        this.name = name;
        this.initialQuantity = initialQuantity;
        this.timestamp = timestamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
