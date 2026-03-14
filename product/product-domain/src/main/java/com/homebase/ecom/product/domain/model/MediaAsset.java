package com.homebase.ecom.product.domain.model;

public class MediaAsset {
    public enum MediaType {
        IMAGE, VIDEO, THREE_D, DOCUMENT
    }

    private String id;
    private String originalUrl;
    private String cdnUrl;
    private MediaType type;
    private String mimeType;
    private long fileSizeBytes;
    private int width;
    private int height;
    private String altText;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getCdnUrl() { return cdnUrl; }
    public void setCdnUrl(String cdnUrl) { this.cdnUrl = cdnUrl; }

    public MediaType getType() { return type; }
    public void setType(MediaType type) { this.type = type; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
}
