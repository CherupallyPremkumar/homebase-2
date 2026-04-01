package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation")
public class OrganisationEntity extends BaseJpaEntity {

    @Column(name = "company_name", nullable = false) private String companyName;
    @Column(name = "legal_name") private String legalName;
    @Column(name = "registration_number") private String registrationNumber;
    @Column(name = "gst_number") private String gstNumber;
    @Column(name = "pan_number") private String panNumber;
    @Column(name = "domain") private String domain;
    @Column(name = "active") private boolean active = true;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrganisationBrandingEntity branding;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrganisationContactEntity contact;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrganisationAddressEntity address;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrganisationLocaleEntity locale;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrganisationSocialEntity social;

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String v) { this.companyName = v; }
    public String getLegalName() { return legalName; }
    public void setLegalName(String v) { this.legalName = v; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String v) { this.registrationNumber = v; }
    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String v) { this.gstNumber = v; }
    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String v) { this.panNumber = v; }
    public String getDomain() { return domain; }
    public void setDomain(String v) { this.domain = v; }
    public boolean isActive() { return active; }
    public void setActive(boolean v) { this.active = v; }
    public OrganisationBrandingEntity getBranding() { return branding; }
    public void setBranding(OrganisationBrandingEntity v) { this.branding = v; }
    public OrganisationContactEntity getContact() { return contact; }
    public void setContact(OrganisationContactEntity v) { this.contact = v; }
    public OrganisationAddressEntity getAddress() { return address; }
    public void setAddress(OrganisationAddressEntity v) { this.address = v; }
    public OrganisationLocaleEntity getLocale() { return locale; }
    public void setLocale(OrganisationLocaleEntity v) { this.locale = v; }
    public OrganisationSocialEntity getSocial() { return social; }
    public void setSocial(OrganisationSocialEntity v) { this.social = v; }
}
