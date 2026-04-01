package com.homebase.ecom.organisation.model;

/**
 * Contact — emails, phones, website. Support team manages this.
 */
public class OrganisationContact {

    private String organisationId;
    private String primaryEmail;
    private String supportEmail;
    private String primaryPhone;
    private String supportPhone;
    private String websiteUrl;

    public String getOrganisationId() { return organisationId; }
    public void setOrganisationId(String organisationId) { this.organisationId = organisationId; }
    public String getPrimaryEmail() { return primaryEmail; }
    public void setPrimaryEmail(String primaryEmail) { this.primaryEmail = primaryEmail; }
    public String getSupportEmail() { return supportEmail; }
    public void setSupportEmail(String supportEmail) { this.supportEmail = supportEmail; }
    public String getPrimaryPhone() { return primaryPhone; }
    public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }
    public String getSupportPhone() { return supportPhone; }
    public void setSupportPhone(String supportPhone) { this.supportPhone = supportPhone; }
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
}
