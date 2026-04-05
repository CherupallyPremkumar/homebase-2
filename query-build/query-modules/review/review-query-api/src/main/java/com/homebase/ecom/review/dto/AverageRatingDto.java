package com.homebase.ecom.review.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AverageRatingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private BigDecimal averageRating;
    private long reviewCount;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public long getReviewCount() { return reviewCount; }
    public void setReviewCount(long reviewCount) { this.reviewCount = reviewCount; }
}
