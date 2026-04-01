package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation_contact")
public class OrganisationContactEntity extends BaseJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationEntity organisation;

    @Column(name = "primary_email") private String primaryEmail;
    @Column(name = "support_email") private String supportEmail;
    @Column(name = "primary_phone") private String primaryPhone;
    @Column(name = "support_phone") private String supportPhone;
    @Column(name = "website_url") private String websiteUrl;

    public OrganisationEntity getOrganisation() { return organisation; }
    public void setOrganisation(OrganisationEntity v) { this.organisation = v; }
    public String getPrimaryEmail() { return primaryEmail; }
    public void setPrimaryEmail(String v) { this.primaryEmail = v; }
    public String getSupportEmail() { return supportEmail; }
    public void setSupportEmail(String v) { this.supportEmail = v; }
    public String getPrimaryPhone() { return primaryPhone; }
    public void setPrimaryPhone(String v) { this.primaryPhone = v; }
    public String getSupportPhone() { return supportPhone; }
    public void setSupportPhone(String v) { this.supportPhone = v; }
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String v) { this.websiteUrl = v; }
}
