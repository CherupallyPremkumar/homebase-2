package com.homebase.ecom.user.domain.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * Address — child entity owned by User aggregate.
 * Extends BaseEntity (non-JPA, chenile-corefork/utils) — same pattern as Rule in policy-domain.
 * Gets: id, createdTime, lastModifiedTime, version (for optimistic locking).
 *
 * Business invariants enforced at the User aggregate level:
 *  - Max 5 addresses per user
 *  - First address auto-becomes default
 *  - Removing the default promotes first remaining address
 *
 * NO JPA annotations — those live in AddressJpaEntity in user-infrastructure.
 */
public class Address extends BaseEntity {

    private String label;    // "Home", "Office", "Warehouse"
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private boolean isDefault;

    /** Called by User aggregate only to promote/demote default. */
    void markAsDefault() { this.isDefault = true; }
    void clearDefault()  { this.isDefault = false; }

    public String getLabel()    { return label; }
    public String getLine1()    { return line1; }
    public String getLine2()    { return line2; }
    public String getCity()     { return city; }
    public String getState()    { return state; }
    public String getZip()      { return zip; }
    public String getCountry()  { return country; }
    public boolean isDefault()  { return isDefault; }

    public void setLabel(String label)   { this.label = label; }
    public void setLine1(String line1)   { this.line1 = line1; }
    public void setLine2(String line2)   { this.line2 = line2; }
    public void setCity(String city)     { this.city = city; }
    public void setState(String state)   { this.state = state; }
    public void setZip(String zip)       { this.zip = zip; }
    public void setCountry(String c)     { this.country = c; }
    public void setDefault(boolean d)    { this.isDefault = d; }
}
