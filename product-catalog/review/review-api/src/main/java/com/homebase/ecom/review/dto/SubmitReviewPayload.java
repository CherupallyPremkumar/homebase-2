package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Payload for submitting a new review (SUBMITTED -> CHECK_AUTO_PUBLISH).
 */
public class SubmitReviewPayload extends MinimalPayload {

    private String productId;
    private String orderId;
    private int rating;
    private String title;
    private String body;
    private List<String> images;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
