package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class BannerQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String imageUrl;
    private String mobileImageUrl;
    private String linkUrl;
    private String position;
    private int displayOrder;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getMobileImageUrl() { return mobileImageUrl; }
    public void setMobileImageUrl(String mobileImageUrl) { this.mobileImageUrl = mobileImageUrl; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
