package com.homebase.ecom.checkout.api.dto;

/**
 * Shipping Option DTO
 */
public class ShippingOptionDTO {

    private String id;
    private String name;
    private String description;
    private MoneyDTO cost;
    private String estimatedDelivery;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoneyDTO getCost() {
        return cost;
    }

    public void setCost(MoneyDTO cost) {
        this.cost = cost;
    }

    public String getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(String estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }
}
