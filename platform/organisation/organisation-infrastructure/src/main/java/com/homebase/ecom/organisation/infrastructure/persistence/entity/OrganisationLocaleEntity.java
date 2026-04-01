package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation_locale")
public class OrganisationLocaleEntity extends BaseJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationEntity organisation;

    @Column(name = "currency") private String currency;
    @Column(name = "timezone") private String timezone;
    @Column(name = "locale") private String locale;
    @Column(name = "date_format") private String dateFormat;
    @Column(name = "country_code") private String countryCode;

    public OrganisationEntity getOrganisation() { return organisation; }
    public void setOrganisation(OrganisationEntity v) { this.organisation = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String v) { this.currency = v; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String v) { this.timezone = v; }
    public String getLocale() { return locale; }
    public void setLocale(String v) { this.locale = v; }
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String v) { this.dateFormat = v; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String v) { this.countryCode = v; }
}
