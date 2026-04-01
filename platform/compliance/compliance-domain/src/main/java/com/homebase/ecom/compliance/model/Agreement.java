package com.homebase.ecom.compliance.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

public class Agreement extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String title;
    private String agreementType;
    private String versionLabel;
    private String contentUrl;
    private String contentHash;
    private String supersededById;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private boolean mandatoryAcceptance;

    private List<AgreementActivity> activities = new ArrayList<>();
    private transient TransientMap transientMap = new TransientMap();

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ActivityLog> obtainActivities() {
        return (Collection<ActivityLog>) (Collection<?>) activities;
    }

    @Override
    public ActivityLog addActivity(String name, String comment) {
        AgreementActivity activity = new AgreementActivity(name, comment);
        activities.add(activity);
        return activity;
    }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAgreementType() { return agreementType; }
    public void setAgreementType(String agreementType) { this.agreementType = agreementType; }
    public String getVersionLabel() { return versionLabel; }
    public void setVersionLabel(String versionLabel) { this.versionLabel = versionLabel; }
    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }
    public String getSupersededById() { return supersededById; }
    public void setSupersededById(String supersededById) { this.supersededById = supersededById; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public boolean isMandatoryAcceptance() { return mandatoryAcceptance; }
    public void setMandatoryAcceptance(boolean mandatoryAcceptance) { this.mandatoryAcceptance = mandatoryAcceptance; }
    public List<AgreementActivity> getActivities() { return activities; }
    public void setActivities(List<AgreementActivity> activities) { this.activities = activities; }
}
