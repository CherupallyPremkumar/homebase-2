package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import java.util.Collection;

@Entity
@Table(name = "platform_policies")
public class PlatformPolicyEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "title")
    private String title;

    @Column(name = "policy_category")
    private String policyCategory;

    @Column(name = "version_label")
    private String versionLabel;

    @Column(name = "content_url", length = 1024)
    private String contentUrl;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "summary_text", length = 2000)
    private String summaryText;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "policy_id")
    private List<PlatformPolicyActivityEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ActivityLog> obtainActivities() {
        return (Collection<ActivityLog>) (Collection<?>) activities;
    }

    @Override
    public ActivityLog addActivity(String name, String comment) {
        PlatformPolicyActivityEntity activity = new PlatformPolicyActivityEntity();
        activity.setActivityName(name);
        activity.setActivityComment(comment);
        activity.setActivitySuccess(true);
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
    public List<PlatformPolicyActivityEntity> getActivities() { return activities; }
    public void setActivities(List<PlatformPolicyActivityEntity> activities) { this.activities = activities; }
}
