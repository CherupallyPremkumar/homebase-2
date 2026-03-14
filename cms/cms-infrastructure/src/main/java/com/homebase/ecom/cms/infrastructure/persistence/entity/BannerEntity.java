package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.util.Date;

@Entity
@Table(name = "banners")
public class BannerEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "mobile_image_url")
    private String mobileImageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "position")
    private String position;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "active_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activeFrom;

    @Column(name = "active_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activeTo;

    @Column(name = "active")
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Date getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
