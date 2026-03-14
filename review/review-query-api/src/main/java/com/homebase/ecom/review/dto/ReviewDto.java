package com.homebase.ecom.review.dto;

import java.io.Serializable;
import java.util.Date;

public class ReviewDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String productId;
    private String userId;
    private int rating;
    private String title;
    private String status;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
