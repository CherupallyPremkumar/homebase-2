package com.homebase.ecom.promo.dto;

import java.io.Serializable;
import java.util.UUID;

public class PromotionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID promotionId;
    private String name;
    private String description;

    public PromotionDto() {
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
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
}
