package com.homebase.ecom.promo.model;

import java.io.Serializable;

public class PromotionDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String description;

    public PromotionDetail(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
