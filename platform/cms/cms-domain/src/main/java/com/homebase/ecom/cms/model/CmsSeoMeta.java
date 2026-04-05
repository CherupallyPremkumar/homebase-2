package com.homebase.ecom.cms.model;

import org.chenile.utils.entity.model.BaseEntity;

public class CmsSeoMeta extends BaseEntity {
    private String pageId;
    private String ogTitle;
    private String ogDescription;
    private String ogImageUrl;
    private String ogType;
    private String twitterCard;
    private String structuredData;
    private String canonicalUrl;
    private String robots;
    private String hreflang;
    private String keywords;
    private String tenant;

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public String getOgTitle() { return ogTitle; }
    public void setOgTitle(String ogTitle) { this.ogTitle = ogTitle; }

    public String getOgDescription() { return ogDescription; }
    public void setOgDescription(String ogDescription) { this.ogDescription = ogDescription; }

    public String getOgImageUrl() { return ogImageUrl; }
    public void setOgImageUrl(String ogImageUrl) { this.ogImageUrl = ogImageUrl; }

    public String getOgType() { return ogType; }
    public void setOgType(String ogType) { this.ogType = ogType; }

    public String getTwitterCard() { return twitterCard; }
    public void setTwitterCard(String twitterCard) { this.twitterCard = twitterCard; }

    public String getStructuredData() { return structuredData; }
    public void setStructuredData(String structuredData) { this.structuredData = structuredData; }

    public String getCanonicalUrl() { return canonicalUrl; }
    public void setCanonicalUrl(String canonicalUrl) { this.canonicalUrl = canonicalUrl; }

    public String getRobots() { return robots; }
    public void setRobots(String robots) { this.robots = robots; }

    public String getHreflang() { return hreflang; }
    public void setHreflang(String hreflang) { this.hreflang = hreflang; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
