package com.homebase.ecom.review.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "product_reviews")
public class ReviewEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(nullable = false)
    private int rating;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "verified_purchase")
    private boolean verifiedPurchase;

    @Column(name = "helpful_count")
    private int helpfulCount;

    @Column(name = "unhelpful_count")
    private int unhelpfulCount;

    @Column(length = 2000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private List<ReviewImageEntity> images = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private List<ReviewActivityLogEntity> activities = new ArrayList<>();

    // --- Getters and Setters ---

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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ReviewImageEntity> getImages() { return images; }
    public void setImages(List<ReviewImageEntity> images) { this.images = images; }

    public List<ReviewActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ReviewActivityLogEntity> activities) { this.activities = activities; }

    // --- Workflow Support ---

    @Override
    public TransientMap getTransientMap() { return this.transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a : activities) {
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReviewActivityLogEntity activityLog = new ReviewActivityLogEntity();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
