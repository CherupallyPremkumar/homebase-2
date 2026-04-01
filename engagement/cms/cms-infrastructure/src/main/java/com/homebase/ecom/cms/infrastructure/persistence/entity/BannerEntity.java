package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banners")
public class BannerEntity extends AbstractJpaStateEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "mobile_image_url")
    private String mobileImageUrl;

    @Column(name = "media_id")
    private String mediaId;

    @Column(name = "mobile_media_id")
    private String mobileMediaId;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "position")
    private String position;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "active_from")
    private LocalDateTime activeFrom;

    @Column(name = "active_to")
    private LocalDateTime activeTo;

    @Column(name = "active")
    private boolean active;

    @Column(name = "click_count")
    private int clickCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    private List<BannerActivityLogEntity> activities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobileImageUrl() {
        return mobileImageUrl;
    }

    public void setMobileImageUrl(String mobileImageUrl) {
        this.mobileImageUrl = mobileImageUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMobileMediaId() {
        return mobileMediaId;
    }

    public void setMobileMediaId(String mobileMediaId) {
        this.mobileMediaId = mobileMediaId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(LocalDateTime activeFrom) {
        this.activeFrom = activeFrom;
    }

    public LocalDateTime getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(LocalDateTime activeTo) {
        this.activeTo = activeTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public List<BannerActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<BannerActivityLogEntity> activities) {
        this.activities = activities;
    }
}
