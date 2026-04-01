package com.homebase.ecom.cms.dto.response;

import java.io.Serializable;

public class MediaResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fileKey;
    private String originalName;
    private String cdnUrl;
    private String mimeType;
    private long fileSizeBytes;
    private int width;
    private int height;
    private String altText;
    private String folder;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFileKey() { return fileKey; }
    public void setFileKey(String fileKey) { this.fileKey = fileKey; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getCdnUrl() { return cdnUrl; }
    public void setCdnUrl(String cdnUrl) { this.cdnUrl = cdnUrl; }

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

    public String getFolder() { return folder; }
    public void setFolder(String folder) { this.folder = folder; }
}
