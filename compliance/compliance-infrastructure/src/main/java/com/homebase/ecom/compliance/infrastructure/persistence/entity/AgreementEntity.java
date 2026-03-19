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
@Table(name = "agreements")
public class AgreementEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "title")
    private String title;

    @Column(name = "agreement_type")
    private String agreementType;

    @Column(name = "version_label")
    private String versionLabel;

    @Column(name = "content_url", length = 1024)
    private String contentUrl;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "superseded_by_id")
    private String supersededById;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "mandatory_acceptance")
    private boolean mandatoryAcceptance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "agreement_id")
    private List<AgreementActivityEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ActivityLog> obtainActivities() {
        return (Collection<ActivityLog>) (Collection<?>) activities;
    }

    @Override
    public ActivityLog addActivity(String name, String comment) {
        AgreementActivityEntity activity = new AgreementActivityEntity();
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
    public List<AgreementActivityEntity> getActivities() { return activities; }
    public void setActivities(List<AgreementActivityEntity> activities) { this.activities = activities; }
}
