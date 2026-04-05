package com.homebase.ecom.recommendation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RecommendationQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String productId;
    private String recommendationType;
    private BigDecimal score;
    private String reason;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
