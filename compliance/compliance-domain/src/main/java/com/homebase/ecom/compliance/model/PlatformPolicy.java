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

public class PlatformPolicy extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String title;
    private String policyCategory;
    private String versionLabel;
    private String contentUrl;
    private String contentHash;
    private String summaryText;
    private LocalDate effectiveDate;

    private List<PlatformPolicyActivity> activities = new ArrayList<>();
    private transient TransientMap transientMap = new TransientMap();

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ActivityLog> obtainActivities() {
        return (Collection<ActivityLog>) (Collection<?>) activities;
    }

    @Override
    public ActivityLog addActivity(String name, String comment) {
        PlatformPolicyActivity activity = new PlatformPolicyActivity(name, comment);
        activities.add(activity);
        return activity;
    }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPolicyCategory() { return policyCategory; }
    public void setPolicyCategory(String policyCategory) { this.policyCategory = policyCategory; }
    public String getVersionLabel() { return versionLabel; }
    public void setVersionLabel(String versionLabel) { this.versionLabel = versionLabel; }
    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }
    public String getSummaryText() { return summaryText; }
    public void setSummaryText(String summaryText) { this.summaryText = summaryText; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public List<PlatformPolicyActivity> getActivities() { return activities; }
    public void setActivities(List<PlatformPolicyActivity> activities) { this.activities = activities; }
}
