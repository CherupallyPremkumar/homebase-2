package com.homebase.ecom.organisation.infrastructure.persistence.mapper;

import com.homebase.ecom.organisation.model.*;
import com.homebase.ecom.organisation.infrastructure.persistence.entity.*;

/**
 * Bidirectional mapper: Organisation domain ↔ JPA entities.
 */
public class OrganisationMapper {

    // ================================================================
    // Entity → Domain
    // ================================================================

    public Organisation toModel(OrganisationEntity e) {
        if (e == null) return null;
        Organisation o = new Organisation();
        o.setId(e.getId());
        o.setCompanyName(e.getCompanyName());
        o.setLegalName(e.getLegalName());
        o.setRegistrationNumber(e.getRegistrationNumber());
        o.setGstNumber(e.getGstNumber());
        o.setPanNumber(e.getPanNumber());
        o.setDomain(e.getDomain());
        o.setActive(e.isActive());

        if (e.getBranding() != null) {
            OrganisationBranding b = new OrganisationBranding();
            b.setOrganisationId(e.getId());
            b.setLogoUrl(e.getBranding().getLogoUrl());
            b.setFaviconUrl(e.getBranding().getFaviconUrl());
            b.setPrimaryColor(e.getBranding().getPrimaryColor());
            b.setCopyrightText(e.getBranding().getCopyrightText());
            b.setFoundedYear(e.getBranding().getFoundedYear());
            o.setBranding(b);
        }
        if (e.getContact() != null) {
            OrganisationContact c = new OrganisationContact();
            c.setOrganisationId(e.getId());
            c.setPrimaryEmail(e.getContact().getPrimaryEmail());
            c.setSupportEmail(e.getContact().getSupportEmail());
            c.setPrimaryPhone(e.getContact().getPrimaryPhone());
            c.setSupportPhone(e.getContact().getSupportPhone());
            c.setWebsiteUrl(e.getContact().getWebsiteUrl());
            o.setContact(c);
        }
        if (e.getAddress() != null) {
            OrganisationAddress a = new OrganisationAddress();
            a.setOrganisationId(e.getId());
            a.setAddressLine1(e.getAddress().getAddressLine1());
            a.setAddressLine2(e.getAddress().getAddressLine2());
            a.setCity(e.getAddress().getCity());
            a.setState(e.getAddress().getState());
            a.setPincode(e.getAddress().getPincode());
            a.setCountry(e.getAddress().getCountry());
            o.setAddress(a);
        }
        if (e.getLocale() != null) {
            OrganisationLocale l = new OrganisationLocale();
            l.setOrganisationId(e.getId());
            l.setCurrency(e.getLocale().getCurrency());
            l.setTimezone(e.getLocale().getTimezone());
            l.setLocale(e.getLocale().getLocale());
            l.setDateFormat(e.getLocale().getDateFormat());
            l.setCountryCode(e.getLocale().getCountryCode());
            o.setLocale(l);
        }
        if (e.getSocial() != null) {
            OrganisationSocial s = new OrganisationSocial();
            s.setOrganisationId(e.getId());
            s.setFacebookUrl(e.getSocial().getFacebookUrl());
            s.setTwitterUrl(e.getSocial().getTwitterUrl());
            s.setInstagramUrl(e.getSocial().getInstagramUrl());
            s.setLinkedinUrl(e.getSocial().getLinkedinUrl());
            s.setYoutubeUrl(e.getSocial().getYoutubeUrl());
            o.setSocial(s);
        }
        return o;
    }

    // ================================================================
    // Domain → Entity
    // ================================================================

    public OrganisationEntity toEntity(Organisation o) {
        if (o == null) return null;
        OrganisationEntity e = new OrganisationEntity();
        e.setId(o.getId());
        e.setCompanyName(o.getCompanyName());
        e.setLegalName(o.getLegalName());
        e.setRegistrationNumber(o.getRegistrationNumber());
        e.setGstNumber(o.getGstNumber());
        e.setPanNumber(o.getPanNumber());
        e.setDomain(o.getDomain());
        e.setActive(o.isActive());

        if (o.getBranding() != null) {
            OrganisationBrandingEntity b = new OrganisationBrandingEntity();
            b.setOrganisation(e);
            b.setLogoUrl(o.getBranding().getLogoUrl());
            b.setFaviconUrl(o.getBranding().getFaviconUrl());
            b.setPrimaryColor(o.getBranding().getPrimaryColor());
            b.setCopyrightText(o.getBranding().getCopyrightText());
            b.setFoundedYear(o.getBranding().getFoundedYear());
            e.setBranding(b);
        }
        if (o.getContact() != null) {
            OrganisationContactEntity c = new OrganisationContactEntity();
            c.setOrganisation(e);
            c.setPrimaryEmail(o.getContact().getPrimaryEmail());
            c.setSupportEmail(o.getContact().getSupportEmail());
            c.setPrimaryPhone(o.getContact().getPrimaryPhone());
            c.setSupportPhone(o.getContact().getSupportPhone());
            c.setWebsiteUrl(o.getContact().getWebsiteUrl());
            e.setContact(c);
        }
        if (o.getAddress() != null) {
            OrganisationAddressEntity a = new OrganisationAddressEntity();
            a.setOrganisation(e);
            a.setAddressLine1(o.getAddress().getAddressLine1());
            a.setAddressLine2(o.getAddress().getAddressLine2());
            a.setCity(o.getAddress().getCity());
            a.setState(o.getAddress().getState());
            a.setPincode(o.getAddress().getPincode());
            a.setCountry(o.getAddress().getCountry());
            e.setAddress(a);
        }
        if (o.getLocale() != null) {
            OrganisationLocaleEntity l = new OrganisationLocaleEntity();
            l.setOrganisation(e);
            l.setCurrency(o.getLocale().getCurrency());
            l.setTimezone(o.getLocale().getTimezone());
            l.setLocale(o.getLocale().getLocale());
            l.setDateFormat(o.getLocale().getDateFormat());
            l.setCountryCode(o.getLocale().getCountryCode());
            e.setLocale(l);
        }
        if (o.getSocial() != null) {
            OrganisationSocialEntity s = new OrganisationSocialEntity();
            s.setOrganisation(e);
            s.setFacebookUrl(o.getSocial().getFacebookUrl());
            s.setTwitterUrl(o.getSocial().getTwitterUrl());
            s.setInstagramUrl(o.getSocial().getInstagramUrl());
            s.setLinkedinUrl(o.getSocial().getLinkedinUrl());
            s.setYoutubeUrl(o.getSocial().getYoutubeUrl());
            e.setSocial(s);
        }
        return e;
    }

    // ================================================================
    // Merge (update existing entity with new values)
    // ================================================================

    public OrganisationEntity mergeEntity(OrganisationEntity existing, OrganisationEntity updated) {
        existing.setCompanyName(updated.getCompanyName());
        existing.setLegalName(updated.getLegalName());
        existing.setRegistrationNumber(updated.getRegistrationNumber());
        existing.setGstNumber(updated.getGstNumber());
        existing.setPanNumber(updated.getPanNumber());
        existing.setDomain(updated.getDomain());
        existing.setActive(updated.isActive());
        // Sub-entities are cascade-managed by JPA
        if (updated.getBranding() != null && existing.getBranding() != null) {
            existing.getBranding().setLogoUrl(updated.getBranding().getLogoUrl());
            existing.getBranding().setFaviconUrl(updated.getBranding().getFaviconUrl());
            existing.getBranding().setPrimaryColor(updated.getBranding().getPrimaryColor());
            existing.getBranding().setCopyrightText(updated.getBranding().getCopyrightText());
            existing.getBranding().setFoundedYear(updated.getBranding().getFoundedYear());
        }
        if (updated.getContact() != null && existing.getContact() != null) {
            existing.getContact().setPrimaryEmail(updated.getContact().getPrimaryEmail());
            existing.getContact().setSupportEmail(updated.getContact().getSupportEmail());
            existing.getContact().setPrimaryPhone(updated.getContact().getPrimaryPhone());
            existing.getContact().setSupportPhone(updated.getContact().getSupportPhone());
            existing.getContact().setWebsiteUrl(updated.getContact().getWebsiteUrl());
        }
        if (updated.getAddress() != null && existing.getAddress() != null) {
            existing.getAddress().setAddressLine1(updated.getAddress().getAddressLine1());
            existing.getAddress().setAddressLine2(updated.getAddress().getAddressLine2());
            existing.getAddress().setCity(updated.getAddress().getCity());
            existing.getAddress().setState(updated.getAddress().getState());
            existing.getAddress().setPincode(updated.getAddress().getPincode());
            existing.getAddress().setCountry(updated.getAddress().getCountry());
        }
        if (updated.getLocale() != null && existing.getLocale() != null) {
            existing.getLocale().setCurrency(updated.getLocale().getCurrency());
            existing.getLocale().setTimezone(updated.getLocale().getTimezone());
            existing.getLocale().setLocale(updated.getLocale().getLocale());
            existing.getLocale().setDateFormat(updated.getLocale().getDateFormat());
            existing.getLocale().setCountryCode(updated.getLocale().getCountryCode());
        }
        if (updated.getSocial() != null && existing.getSocial() != null) {
            existing.getSocial().setFacebookUrl(updated.getSocial().getFacebookUrl());
            existing.getSocial().setTwitterUrl(updated.getSocial().getTwitterUrl());
            existing.getSocial().setInstagramUrl(updated.getSocial().getInstagramUrl());
            existing.getSocial().setLinkedinUrl(updated.getSocial().getLinkedinUrl());
            existing.getSocial().setYoutubeUrl(updated.getSocial().getYoutubeUrl());
        }
        return existing;
    }
}
