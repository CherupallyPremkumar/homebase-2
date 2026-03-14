package com.homebase.ecom.review.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "review_images")
public class ReviewImageEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String url;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "display_order")
    private int displayOrder;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
