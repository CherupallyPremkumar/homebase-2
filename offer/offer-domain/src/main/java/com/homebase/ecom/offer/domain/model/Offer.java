package com.homebase.ecom.offer.domain.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.shared.Money;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Offer extends AbstractExtendedStateEntity 
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String variantId; // Links to Product BC Variant
    private String supplierId; // Maker's ID
    private Money price;
    private Money msrp;
    private OfferStatus status;
    private java.util.Date trialEndDate;
    private TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Money getMsrp() {
        return msrp;
    }

    public void setMsrp(Money msrp) {
        this.msrp = msrp;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public java.util.Date getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(java.util.Date trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return activities;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        OfferActivityLog log = new OfferActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        this.activities.add(log);
        return log;
    }

    // Domain logic: Is the offer eligible for sale?
    public boolean isSalable() {
        return status == OfferStatus.ACTIVE;
    }
}
