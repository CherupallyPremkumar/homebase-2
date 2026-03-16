package com.homebase.ecom.product.domain.model;

public class MediaAsset {
    public enum MediaType {
        IMAGE, VIDEO, THREE_D, DOCUMENT
    }

    public enum ProcessingStatus {
        PENDING,      // Upload registered, not yet processed
        PROCESSING,   // Resize/conversion in progress
        COMPLETED,    // All variants generated, CDN ready
        FAILED        // Processing failed
    }

    private String id;
    private String originalUrl;
    private String cdnUrl;
    private String thumbnailUrl;
    private String mediumUrl;
    private String zoomUrl;
    private MediaType type;
    private String mimeType;
    private long fileSizeBytes;
    private int width;
    private int height;
    private String altText;
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

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

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getMediumUrl() { return mediumUrl; }
    public void setMediumUrl(String mediumUrl) { this.mediumUrl = mediumUrl; }

    public String getZoomUrl() { return zoomUrl; }
    public void setZoomUrl(String zoomUrl) { this.zoomUrl = zoomUrl; }

    public ProcessingStatus getProcessingStatus() { return processingStatus; }
    public void setProcessingStatus(ProcessingStatus processingStatus) { this.processingStatus = processingStatus; }
}
