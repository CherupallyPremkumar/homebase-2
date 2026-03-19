package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CategoryMetadataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String bannerImageUrl;
    private Boolean showInMenu;
    private Boolean showInFooter;

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public String getMetaKeywords() { return metaKeywords; }
    public void setMetaKeywords(String metaKeywords) { this.metaKeywords = metaKeywords; }

    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }

    public Boolean getShowInMenu() { return showInMenu; }
    public void setShowInMenu(Boolean showInMenu) { this.showInMenu = showInMenu; }

    public Boolean getShowInFooter() { return showInFooter; }
    public void setShowInFooter(Boolean showInFooter) { this.showInFooter = showInFooter; }
}
