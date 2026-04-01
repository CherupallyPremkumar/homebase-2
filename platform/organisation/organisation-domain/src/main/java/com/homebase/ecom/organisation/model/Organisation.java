package com.homebase.ecom.organisation.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * Organisation — aggregate root. The business entity that runs this marketplace.
 * One record per tenant. The inherited 'tenant' field from BaseEntity IS the tenant ID.
 * Every other table's 'tenant' column references this Organisation's tenant value.
 *
 * Example: Organisation with tenant="homebase" → all orders, products, users
 * have tenant="homebase" in their rows.
 */
public class Organisation extends BaseEntity {

    private String companyName;
    private String legalName;
    private String registrationNumber;
    private String gstNumber;
    private String panNumber;
    private String domain;                // homebase.com
    private boolean active = true;

    private OrganisationBranding branding;
    private OrganisationContact contact;
    private OrganisationAddress address;
    private OrganisationLocale locale;
    private OrganisationSocial social;

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OrganisationBranding getBranding() { return branding; }
    public void setBranding(OrganisationBranding branding) { this.branding = branding; }
    public OrganisationContact getContact() { return contact; }
    public void setContact(OrganisationContact contact) { this.contact = contact; }
    public OrganisationAddress getAddress() { return address; }
    public void setAddress(OrganisationAddress address) { this.address = address; }
    public OrganisationLocale getLocale() { return locale; }
    public void setLocale(OrganisationLocale locale) { this.locale = locale; }
    public OrganisationSocial getSocial() { return social; }
    public void setSocial(OrganisationSocial social) { this.social = social; }
}
