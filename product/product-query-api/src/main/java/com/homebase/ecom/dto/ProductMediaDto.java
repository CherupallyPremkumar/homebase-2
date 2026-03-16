package com.homebase.ecom.dto;

import java.io.Serializable;

public class ProductMediaDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String assetId;
    private String productId;
    private String cdnUrl;
    private String thumbnailUrl;
    private String mediumUrl;
    private String zoomUrl;
    private String type;
    private String altText;
    private boolean primary;
    private int sortOrder;

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getCdnUrl() { return cdnUrl; }
    public void setCdnUrl(String cdnUrl) { this.cdnUrl = cdnUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getMediumUrl() { return mediumUrl; }
    public void setMediumUrl(String mediumUrl) { this.mediumUrl = mediumUrl; }
    public String getZoomUrl() { return zoomUrl; }
    public void setZoomUrl(String zoomUrl) { this.zoomUrl = zoomUrl; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
