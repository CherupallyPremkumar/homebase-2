package com.homebase.ecom.product.infrastructure.persistence.entity;

import com.homebase.ecom.product.domain.model.MediaAsset.MediaType;
import jakarta.persistence.*;

@Entity
@Table(name = "media_assets")
public class MediaAssetEntity {
    @Id
    private String id;

    @Column(name = "original_url", length = 1000)
    private String originalUrl;

    @Column(name = "cdn_url", length = 1000)
    private String cdnUrl;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size_bytes")
    private long fileSizeBytes;

    private int width;
    private int height;

    @Column(name = "alt_text")
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
