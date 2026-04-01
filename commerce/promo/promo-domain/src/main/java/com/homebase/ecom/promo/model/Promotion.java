package com.homebase.ecom.promo.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "promotions")
public class Promotion extends AbstractJpaStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "promo_type")
    private String promoType; // e.g., DISCOUNT, BUY_X_GET_Y

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String rules; // JSON representing rules and conditions

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String strategy; // JSON representing calculation strategy

    @Column(nullable = false)
    private Integer priority = 0;

    @Column(nullable = false)
    private Boolean stackable = false;

    @Column(nullable = false)
    private boolean active = true;

    private java.time.LocalDateTime validFrom;
    private java.time.LocalDateTime validUntil;

    @Transient
    private TransientMap transientMap = new TransientMap();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "promotion_id")
    private List<PromoCodeActivityLog> activities = new ArrayList<>();

    public Promotion() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public java.time.LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(java.time.LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public java.time.LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(java.time.LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public List<PromoCodeActivityLog> getActivities() {
        return activities;
    }

    public void setActivities(List<PromoCodeActivityLog> activities) {
        this.activities = activities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Promotion))
            return false;
        if (!super.equals(o))
            return false;
        Promotion promotion = (Promotion) o;
        return active == promotion.active &&
                java.util.Objects.equals(name, promotion.name) &&
                java.util.Objects.equals(description, promotion.description) &&
                java.util.Objects.equals(promoType, promotion.promoType) &&
                java.util.Objects.equals(rules, promotion.rules) &&
                java.util.Objects.equals(strategy, promotion.strategy) &&
                java.util.Objects.equals(priority, promotion.priority) &&
                java.util.Objects.equals(stackable, promotion.stackable) &&
                java.util.Objects.equals(validFrom, promotion.validFrom) &&
                java.util.Objects.equals(validUntil, promotion.validUntil) &&
                java.util.Objects.equals(activities, promotion.activities);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), name, description, promoType, rules, strategy, priority,
                stackable, active, validFrom, validUntil, activities);
    }

    // --- Business Logic Placeholders ---

    public boolean isApplicable() {
        return active && isInValidDateRange();
    }

    public boolean isInValidDateRange() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (validFrom != null && now.isBefore(validFrom))
            return false;
        if (validUntil != null && now.isAfter(validUntil))
            return false;
        return true;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        PromoCodeActivityLog activityLog = new PromoCodeActivityLog();
        activityLog.setActivityName(eventId);
        activityLog.setActivityComment(comment);
        activityLog.setActivitySuccess(true);
        activities.add(activityLog);
        return activityLog;
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }
}
