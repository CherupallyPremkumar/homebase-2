package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation_address")
public class OrganisationAddressEntity extends BaseJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationEntity organisation;

    @Column(name = "address_line1") private String addressLine1;
    @Column(name = "address_line2") private String addressLine2;
    @Column(name = "city") private String city;
    @Column(name = "state") private String state;
    @Column(name = "pincode") private String pincode;
    @Column(name = "country") private String country;

    public OrganisationEntity getOrganisation() { return organisation; }
    public void setOrganisation(OrganisationEntity v) { this.organisation = v; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String v) { this.addressLine1 = v; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String v) { this.addressLine2 = v; }
    public String getCity() { return city; }
    public void setCity(String v) { this.city = v; }
    public String getState() { return state; }
    public void setState(String v) { this.state = v; }
    public String getPincode() { return pincode; }
    public void setPincode(String v) { this.pincode = v; }
    public String getCountry() { return country; }
    public void setCountry(String v) { this.country = v; }
}
