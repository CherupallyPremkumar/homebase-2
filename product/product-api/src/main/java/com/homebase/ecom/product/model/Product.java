package com.homebase.ecom.product.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.shared.model.Money;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "product")
public class Product extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {
    @Column(nullable = false)
    private String name;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(length = 100)
    private String category;

    @Column(length = 2000)
    public String description;
    @Transient
    public TransientMap transientMap = new TransientMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    public List<ProductActivityLog> activities = new ArrayList<>();

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
        ProductActivityLog activityLog = new ProductActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
