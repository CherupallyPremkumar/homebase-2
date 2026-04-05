package com.homebase.ecom.cms.dto;

import java.io.Serializable;
import java.util.Date;

public class CmsSeoMetaQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pageId;
    private String ogTitle;
    private String ogType;
    private String canonicalUrl;
    private String robots;
    private String keywords;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }
    public String getOgTitle() { return ogTitle; }
    public void setOgTitle(String ogTitle) { this.ogTitle = ogTitle; }
    public String getOgType() { return ogType; }
    public void setOgType(String ogType) { this.ogType = ogType; }
    public String getCanonicalUrl() { return canonicalUrl; }
    public void setCanonicalUrl(String canonicalUrl) { this.canonicalUrl = canonicalUrl; }
    public String getRobots() { return robots; }
    public void setRobots(String robots) { this.robots = robots; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
