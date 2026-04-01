package com.homebase.ecom.organisation.model;

/**
 * Branding — logo, favicon, colors, copyright. Design team manages this.
 */
public class OrganisationBranding {

    private String organisationId;
    private String logoUrl;
    private String faviconUrl;
    private String primaryColor;
    private String copyrightText;
    private int foundedYear;

    public String getOrganisationId() { return organisationId; }
    public void setOrganisationId(String organisationId) { this.organisationId = organisationId; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getFaviconUrl() { return faviconUrl; }
    public void setFaviconUrl(String faviconUrl) { this.faviconUrl = faviconUrl; }
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    public String getCopyrightText() { return copyrightText; }
    public void setCopyrightText(String copyrightText) { this.copyrightText = copyrightText; }
    public int getFoundedYear() { return foundedYear; }
    public void setFoundedYear(int foundedYear) { this.foundedYear = foundedYear; }
}
