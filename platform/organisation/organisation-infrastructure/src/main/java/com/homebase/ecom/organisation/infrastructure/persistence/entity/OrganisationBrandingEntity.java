package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation_branding")
public class OrganisationBrandingEntity extends BaseJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationEntity organisation;

    @Column(name = "logo_url") private String logoUrl;
    @Column(name = "favicon_url") private String faviconUrl;
    @Column(name = "primary_color") private String primaryColor;
    @Column(name = "copyright_text") private String copyrightText;
    @Column(name = "founded_year") private int foundedYear;

    public OrganisationEntity getOrganisation() { return organisation; }
    public void setOrganisation(OrganisationEntity v) { this.organisation = v; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String v) { this.logoUrl = v; }
    public String getFaviconUrl() { return faviconUrl; }
    public void setFaviconUrl(String v) { this.faviconUrl = v; }
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String v) { this.primaryColor = v; }
    public String getCopyrightText() { return copyrightText; }
    public void setCopyrightText(String v) { this.copyrightText = v; }
    public int getFoundedYear() { return foundedYear; }
    public void setFoundedYear(int v) { this.foundedYear = v; }
}
