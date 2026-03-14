package com.homebase.ecom.cms.dto;

import java.io.Serializable;
import java.util.Date;

public class BannerQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String imageUrl;
    private String position;
    private boolean active;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
