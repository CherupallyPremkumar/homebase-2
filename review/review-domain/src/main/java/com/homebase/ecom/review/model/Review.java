package com.homebase.ecom.review.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Review extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String productId;
    private String userId;
    private String orderId;
    private int rating;
    private String title;
    private String body;
    private boolean verifiedPurchase;
    private int helpfulCount;
    private int unhelpfulCount;
    public String description;

    private List<ReviewImage> images = new ArrayList<>();

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public int getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; }

    public int getUnhelpfulCount() { return unhelpfulCount; }
    public void setUnhelpfulCount(int unhelpfulCount) { this.unhelpfulCount = unhelpfulCount; }

    public List<ReviewImage> getImages() { return images; }
    public void setImages(List<ReviewImage> images) { this.images = images; }

    public TransientMap getTransientMap() { return this.transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReviewActivityLog activityLog = new ReviewActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
