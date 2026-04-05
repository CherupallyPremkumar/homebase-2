package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.util.List;

/**
 * Payload for resubmitting a review after edit request (EDIT_REQUESTED -> UNDER_MODERATION).
 */
public class ResubmitReviewPayload extends MinimalPayload {
    private int rating;
    private String title;
    private String body;
    private List<String> images;

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
