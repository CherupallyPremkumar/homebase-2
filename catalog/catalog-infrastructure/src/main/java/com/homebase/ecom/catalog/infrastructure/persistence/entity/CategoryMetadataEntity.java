package com.homebase.ecom.catalog.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * CategoryMetadataEntity - Persistence Value Object
 */
@Embeddable
public class CategoryMetadataEntity {
    
    @Column(name = "meta_title", length = 100)
    private String metaTitle;
    
    @Column(name = "meta_description", length = 200)
    private String metaDescription;
    
    @Column(name = "meta_keywords", length = 200)
    private String metaKeywords;
    
    @Column(name = "banner_image_url", length = 500)
    private String bannerImageUrl;
    
    @Column(name = "show_in_menu")
    private Boolean showInMenu = true;
    
    @Column(name = "show_in_footer")
    private Boolean showInFooter = false;

    // Getters and Setters

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public Boolean getShowInMenu() {
        return showInMenu;
    }

    public void setShowInMenu(Boolean showInMenu) {
        this.showInMenu = showInMenu;
    }

    public Boolean getShowInFooter() {
        return showInFooter;
    }

    public void setShowInFooter(Boolean showInFooter) {
        this.showInFooter = showInFooter;
    }
}
